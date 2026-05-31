package com.sakura.flowdrive

import android.app.Application
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.core.util.Logger
import com.sakura.flowdrive.crash.CrashHandler

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppSettings.init(this)
        Logger.initialize(this)
        CrashHandler.init(this)
    }
}
