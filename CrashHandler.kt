package com.sakura.quantumplanet.core.utils

import android.content.Context
import android.os.Process
import com.sakura.quantumplanet.core.ui.CrashActivity
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

object CrashHandler {

    private lateinit var applicationContext: Context
    private var isHandling = false // 防止重复触发崩溃逻辑

    fun init(context: Context) {
        applicationContext = context.applicationContext
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (isHandling) return@setDefaultUncaughtExceptionHandler
            isHandling = true
            
            try {
                handleException(thread, throwable)
            } catch (e: Exception) {
                // 如果处理过程报错，强制退出，防止僵尸进程
                exitProcess(1)
            }
        }
    }

    private fun handleException(thread: Thread, throwable: Throwable) {
        // 1. 获取堆栈
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable.printStackTrace(pw)
        val stackTrace = sw.toString()

        @Suppress("DEPRECATION")
        val errorLog = """
[Crash Report]
Time: ${java.util.Date()}
Thread: ${thread.name} (ID: ${thread.id})
Device: ${android.os.Build.MODEL} (API ${android.os.Build.VERSION.SDK_INT})
            
Exception: ${throwable.javaClass.simpleName}
Message: ${throwable.localizedMessage}
            
Stack Trace:
$stackTrace
        """.trimIndent()

        // 2. 第一道保险
        Logger.e("CRASH_MASTER", errorLog)

        // 3. 尝试启动子进程救援页面
        try {
            CrashActivity.start(applicationContext, errorLog)
        } catch (e: Exception) {
        }

        // 4. 彻底杀掉当前进程
        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}