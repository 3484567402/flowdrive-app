package com.sakura.flowdrive

import android.app.Application
import com.sakura.flowdrive.CrashActivity
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.core.util.CrashHandler
import com.sakura.flowdrive.core.util.Logger

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppSettings.init(this)
        Logger.initialize(this)
        CrashHandler.init(this)
        CrashHandler.setCrashCallback { context, crashInfo ->
            CrashActivity.start(context, crashInfo)
        }
    }
}
