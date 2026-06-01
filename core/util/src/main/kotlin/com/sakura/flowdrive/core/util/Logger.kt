package com.sakura.flowdrive.core.util

import android.content.Context
import android.util.Log
import com.tencent.mmkv.MMKV
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

object Logger {
    private const val KEY_LOG_PATH = "log_path"

    private lateinit var appContext: Context

    private var cachedLogPath: String? = null

    private const val VERBOSE = 0
    private const val DEBUG = 1
    private const val INFO = 2
    private const val WARN = 3
    private const val ERROR = 4

    private val executor = Executors.newSingleThreadExecutor()

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var currentLogDate: String? = null
    private var currentWriter: BufferedWriter? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
        ensureLogDirectory()

        val configInfo = buildString {
            appendLine("=== Logger 初始化完成 ===")
            appendLine("日志路径: ${getLogPath()}")
            appendLine("MMKV可用: ${isMMKVAvailable()}")
            appendLine("========================")
        }

        internalLog(INFO, "Logger", configInfo, false)
    }

    fun setLogPath(customPath: String) {
        try {
            val mmkv = MMKV.defaultMMKV()
            mmkv.encode(KEY_LOG_PATH, customPath)
            cachedLogPath = customPath
            closeWriter()
            ensureLogDirectory(customPath)
            internalLog(INFO, "Logger", "日志路径已更新: $customPath", false)
        } catch (e: Exception) {
            Log.e("Logger", "设置日志路径失败: ${e.message}")
        }
    }

    fun getLogPath(): String {
        cachedLogPath?.let { return it }
        val path = try {
            val mmkv = MMKV.defaultMMKV()
            mmkv.decodeString(KEY_LOG_PATH, getDefaultLogPath()) ?: getDefaultLogPath()
        } catch (e: Exception) {
            Log.e("Logger", "获取日志路径失败: ${e.message}")
            getDefaultLogPath()
        }
        cachedLogPath = path
        return path
    }

    fun v(tag: String, message: String) = log(VERBOSE, tag, message)
    fun d(tag: String, message: String) = log(DEBUG, tag, message)
    fun i(tag: String, message: String) = log(INFO, tag, message)
    fun w(tag: String, message: String) = log(WARN, tag, message)
    fun e(tag: String, message: String) = log(ERROR, tag, message)
    fun e(tag: String, message: String, throwable: Throwable) =
        log(ERROR, tag, "$message\n${Log.getStackTraceString(throwable)}")

    private fun log(level: Int, tag: String, message: String) {
        logToLogcat(level, tag, message)
        logToFile(level, tag, message)
    }

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

    private fun logToFile(level: Int, tag: String, message: String) {
        executor.submit {
            try {
                internalLog(level, tag, message, true)
            } catch (e: Exception) {
                Log.e("Logger", "写入日志文件失败: ${e.message}")
            }
        }
    }

    private fun internalLog(level: Int, tag: String, message: String, includeStackTrace: Boolean) {
        try {
            val logPath = getLogPath()
            val todayDate = LocalDate.now().format(dateFormatter)

            val writer = getWriter(logPath, todayDate)
            val logFile = File(logPath, "app_$todayDate.log")
            if (!logFile.exists()) {
                logFile.parentFile?.mkdirs()
                logFile.createNewFile()
                val timeStr = Instant.now().atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
                writer.write("=== 日志文件创建时间: $timeStr ===\n")
                writer.flush()
            }

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

            val timeStr = Instant.now().atZone(ZoneId.systemDefault()).format(dateTimeFormatter)

            val logContent = buildString {
                append(timeStr)
                append(" $levelStr/$tag: ")
                append(message)
                if (stackTraceInfo.isNotEmpty()) {
                    append(" | $stackTraceInfo")
                }
                append("\n")
            }

            writer.write(logContent)
            writer.flush()
        } catch (e: Exception) {
            Log.e("Logger", "internalLog 异常: ${e.message}")
        }
    }

    @Synchronized
    private fun getWriter(logPath: String, todayDate: String): BufferedWriter {
        val existing = currentWriter
        if (currentLogDate == todayDate && existing != null) {
            return existing
        }
        existing?.close()
        val logFile = File(logPath, "app_$todayDate.log")
        val writer = BufferedWriter(FileWriter(logFile, true))
        currentLogDate = todayDate
        currentWriter = writer
        return writer
    }

    @Synchronized
    private fun closeWriter() {
        currentWriter?.close()
        currentWriter = null
        currentLogDate = null
    }

    private fun getStackTraceInfo(): String {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            for (i in stackTrace.indices) {
                val element = stackTrace[i]
                if (element.className.contains("Logger") && i + 1 < stackTrace.size) {
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

    private fun ensureLogDirectory(path: String = getLogPath()) {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    private fun isMMKVAvailable(): Boolean {
        return try {
            MMKV.defaultMMKV()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun getDefaultLogPath(): String {
        return if (::appContext.isInitialized) {
            val externalDir = appContext.getExternalFilesDir(null)
            if (externalDir != null) {
                File(externalDir, "logs").absolutePath
            } else {
                File(appContext.filesDir, "logs").absolutePath
            }
        } else {
            "/storage/emulated/0/Android/data/com.sakura.flowdrive/logs"
        }
    }

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

    fun cleanupOldLogs(daysToKeep: Int = 7) {
        executor.submit {
            try {
                val logFiles = getLogFiles()
                val cutoffTime = System.currentTimeMillis() - daysToKeep.toLong() * 24 * 60 * 60 * 1000

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
