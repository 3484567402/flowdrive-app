package com.sakura.flowdrive.core.util

import android.content.Context
import android.content.Intent
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

object CrashHandler {

    private lateinit var applicationContext: Context
    private var isHandling = false
    private var crashActivityClassName: String? = null

    fun init(context: Context) {
        applicationContext = context.applicationContext
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (isHandling) return@setDefaultUncaughtExceptionHandler
            isHandling = true

            try {
                handleException(thread, throwable)
            } catch (e: Exception) {
                exitProcess(1)
            }
        }
    }

    fun setCrashActivity(className: String) {
        crashActivityClassName = className
    }

    private fun handleException(thread: Thread, throwable: Throwable) {
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

        Logger.e("CRASH_MASTER", errorLog)

        try {
            crashActivityClassName?.let { className ->
                val intent = Intent(applicationContext, Class.forName(className)).apply {
                    putExtra("crash_log", errorLog)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                applicationContext.startActivity(intent)
            }
        } catch (e: Exception) {
        }

        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}
