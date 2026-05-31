package com.sakura.flowdrive.crash

import android.content.Context
import android.os.Process
import com.sakura.flowdrive.core.util.Logger
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

object CrashHandler {

    private lateinit var applicationContext: Context
    private var isHandling = false

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
            CrashActivity.start(applicationContext, errorLog)
        } catch (e: Exception) {
        }

        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}
