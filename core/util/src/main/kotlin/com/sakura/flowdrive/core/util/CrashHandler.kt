package com.sakura.flowdrive.core.util

import android.content.Context
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

object CrashHandler {

    private lateinit var applicationContext: Context

    @Volatile
    private var isHandling = false

    private var crashActivityStarter: ((Context, String) -> Unit)? = null

    private val crashTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun init(context: Context, crashStarter: ((Context, String) -> Unit)? = null) {
        applicationContext = context.applicationContext
        crashActivityStarter = crashStarter
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

        val timeStr = Instant.now().atZone(ZoneId.systemDefault()).format(crashTimeFormatter)

        val errorLog = buildString {
            appendLine("[Crash Report]")
            appendLine("Time: $timeStr")
            appendLine("Thread: ${thread.name} (tid=${thread.threadId})")
            appendLine("Device: ${android.os.Build.MODEL} (API ${android.os.Build.VERSION.SDK_INT})")
            appendLine()
            appendLine("Exception: ${throwable.javaClass.simpleName}")
            appendLine("Message: ${throwable.localizedMessage}")
            appendLine()
            appendLine("Stack Trace:")
            append(stackTrace)
        }

        Logger.e("CRASH_MASTER", errorLog)

        try {
            crashActivityStarter?.invoke(applicationContext, errorLog)
        } catch (_: Exception) {
        }

        Process.killProcess(Process.myPid())
        exitProcess(10)
    }
}
