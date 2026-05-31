package com.sakura.flowdrive

import android.app.Application
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.core.util.CrashHandler
import com.sakura.flowdrive.core.util.Logger

class App : Application() {
    override fun attachBaseContext(base: android.content.Context) {
        AppSettings.init(base)
        super.attachBaseContext(AppSettings.applyLocale(base))
    }

    override fun onCreate() {
        super.onCreate()
        Logger.initialize(this)
        CrashHandler.init(this) { context, crashInfo ->
            CrashActivity.start(context, crashInfo)
        }
    }
}
