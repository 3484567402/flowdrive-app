package com.sakura.flowdrive

import android.app.Application
import android.content.Context
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.core.util.CrashHandler
import com.sakura.flowdrive.core.util.Logger

class App : Application() {
    override fun attachBaseContext(base: Context) {
        AppSettings.initMMKV(base)
        AppSettings.init()
        super.attachBaseContext(AppSettings.applyLocaleEarly(base))
    }

    override fun onCreate() {
        super.onCreate()
        Logger.initialize(this)
        CrashHandler.init(this) { context, crashInfo ->
            CrashActivity.start(context, crashInfo)
        }
    }
}
