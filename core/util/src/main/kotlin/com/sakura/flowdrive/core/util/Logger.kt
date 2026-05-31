package com.sakura.flowdrive.core.util

import android.content.Context
import android.util.Log
import com.tencent.mmkv.MMKV
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

object Logger {
    // MMKV配置键名
    private const val KEY_LOG_PATH = "log_path"

    // 应用上下文（需要在Application中初始化）
    private lateinit var appContext: Context

    // 动态获取路径
    private fun getDefaultLogPath(): String {
        return if (::appContext.isInitialized) {
            // 使用应用私有目录 + logs
            val externalDir = appContext.getExternalFilesDir(null)
            if (externalDir != null) {
                File(externalDir, "logs").absolutePath
            } else {
                // Fallback to internal cache if external is null
                File(appContext.filesDir, "logs").absolutePath
            }
        } else {
            // 备用方案，但这种情况不应该发生
            "/storage/emulated/0/Android/data/com.sakura.flowdrive/logs"
        }
    }

    // 日志级别
    private const val VERBOSE = 0
    private const val DEBUG = 1
    private const val INFO = 2
    private const val WARN = 3
    private const val ERROR = 4

    // 线程池用于异步写入
    private val executor = Executors.newSingleThreadExecutor()

    // 日期格式 (SimpleDateFormat非线程安全，使用时需同步)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
    private val fileDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /**
     * 初始化日志系统
     */
    fun initialize(context: Context) {
        appContext = context.applicationContext

        // 确保日志目录存在
        ensureLogDirectory()

        // 记录初始化日志
        val configInfo = """
            |=== Logger 初始化完成 ===
            |日志路径: ${getLogPath()}
            |========================
        """.trimMargin()

        internalLog(INFO, "Logger", configInfo, false)
    }

    /**
     * 设置自定义日志路径
     */
    fun setLogPath(customPath: String) {
        try {
            val mmkv = MMKV.defaultMMKV()
            mmkv.encode(KEY_LOG_PATH, customPath)

            // 确保新目录存在
            ensureLogDirectory(customPath)

            internalLog(INFO, "Logger", "日志路径已更新: $customPath", false)
        } catch (e: Exception) {
            Log.e("Logger", "设置日志路径失败: ${e.message}")
        }
    }

    /**
     * 获取当前日志路径
     */
    fun getLogPath(): String {
        return try {
            val mmkv = MMKV.defaultMMKV()
            // 修复：使用动态获取的默认路径
            mmkv.decodeString(KEY_LOG_PATH, getDefaultLogPath()) ?: getDefaultLogPath()
        } catch (e: Exception) {
            Log.e("Logger", "获取日志路径失败: ${e.message}")
            getDefaultLogPath()
        }
    }

    // region 公共日志方法
    fun v(tag: String, message: String) = log(VERBOSE, tag, message)
    fun d(tag: String, message: String) = log(DEBUG, tag, message)
    fun i(tag: String, message: String) = log(INFO, tag, message)
    fun w(tag: String, message: String) = log(WARN, tag, message)
    fun e(tag: String, message: String) = log(ERROR, tag, message)
    fun e(tag: String, message: String, throwable: Throwable) =
        log(ERROR, tag, "$message\n${Log.getStackTraceString(throwable)}")
    // endregion

    /**
     * 统一的日志处理方法
     */
    private fun log(level: Int, tag: String, message: String) {
        // 同时输出到Logcat和文件
        logToLogcat(level, tag, message)
        logToFile(level, tag, message)
    }

    /**
     * 输出到Logcat
     * 优化：恢复了正确的分级输出，便于在Logcat中过滤颜色
     */
    private fun logToLogcat(level: Int, tag: String, message: String) {
        when (level) {
            VERBOSE -> Log.v(tag, message)
            DEBUG -> Log.d(tag, message)
            INFO -> Log.i(tag, message)
            WARN -> Log.w(tag, message)
            ERROR -> Log.e(tag, message)
            else -> Log.i(tag, message)
        }
    }

    /**
     * 异步写入文件
     */
    private fun logToFile(level: Int, tag: String, message: String) {
        executor.submit {
            try {
                internalLog(level, tag, message, true)
            } catch (e: Exception) {
                // 防止后台线程崩溃
                Log.e("Logger", "写入日志文件失败: ${e.message}")
            }
        }
    }

    /**
     * 内部日志实现
     */
    private fun internalLog(level: Int, tag: String, message: String, includeStackTrace: Boolean) {
        try {
            val logPath = getLogPath()
            val logFile = getTodayLogFile(logPath)

            val stackTraceInfo = if (includeStackTrace) {
                getStackTraceInfo()
            } else ""

            val levelStr = when (level) {
                VERBOSE -> "V"
                DEBUG -> "D"
                INFO -> "I"
                WARN -> "W"
                ERROR -> "E"
                else -> "U"
            }

            // 优化：加锁处理日期格式化，防止多线程竞争
            val timeStr = synchronized(dateFormat) {
                dateFormat.format(Date())
            }

            val logContent = buildString {
                append(timeStr)
                append(" $levelStr/$tag: ")
                append(message)
                if (stackTraceInfo.isNotEmpty()) {
                    append(" | $stackTraceInfo")
                }
                append("\n")
            }

            logFile.appendText(logContent)
        } catch (e: Exception) {
            Log.e("Logger", "internalLog 异常: ${e.message}")
        }
    }

    /**
     * 获取调用栈信息
     */
    private fun getStackTraceInfo(): String {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            for (i in stackTrace.indices) {
                val element = stackTrace[i]
                if (element.className.contains("Logger") && i + 1 < stackTrace.size) { // 更新了类名引用
                    val caller = stackTrace[i + 1]
                    val className = caller.className.substringAfterLast(".")
                    val methodName = caller.methodName
                    val lineNumber = caller.lineNumber
                    return "$className.$methodName($lineNumber)"
                }
            }
            "Unknown"
        } catch (e: Exception) {
            "Error:${e.message}"
        }
    }

    /**
     * 获取今天的日志文件
     */
    private fun getTodayLogFile(logPath: String): File {
        // 优化：加锁处理文件名日期，防止跨天时的并发问题
        val dateStr = synchronized(fileDateFormat) {
            fileDateFormat.format(Date())
        }
        val logFile = File(logPath, "app_$dateStr.log")

        if (!logFile.exists()) {
            logFile.parentFile?.mkdirs()
            logFile.createNewFile()
            // 写入文件头
            val timeStr = synchronized(dateFormat) {
                dateFormat.format(Date())
            }
            logFile.writeText("=== 日志文件创建时间: $timeStr ===\n")
        }

        return logFile
    }

    /**
     * 确保日志目录存在
     */
    private fun ensureLogDirectory(path: String = getLogPath()) {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    /**
     * 获取所有日志文件列表
     */
    fun getLogFiles(): List<File> {
        return try {
            val logDir = File(getLogPath())
            if (logDir.exists() && logDir.isDirectory) {
                logDir.listFiles { file ->
                    file.isFile && file.name.startsWith("app_") && file.name.endsWith(".log")
                }?.sortedByDescending { it.lastModified() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Logger", "获取日志文件列表失败: ${e.message}")
            emptyList()
        }
    }

    /**
     * 清理过期日志文件（保留最近n天）
     */
    fun cleanupOldLogs(daysToKeep: Int = 7) {
        executor.submit {
            try {
                val logFiles = getLogFiles()
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -daysToKeep)
                val cutoffTime = calendar.timeInMillis

                var deletedCount = 0
                logFiles.forEach { file ->
                    if (file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            deletedCount++
                        }
                    }
                }

                if (deletedCount > 0) {
                    internalLog(INFO, "Logger", "清理了 $deletedCount 个过期日志文件", false)
                }
            } catch (e: Exception) {
                Log.e("Logger", "清理日志文件失败: ${e.message}")
            }
        }
    }
}
