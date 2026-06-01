package com.sakura.flowdrive

import android.app.Application
import android.content.Context
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.core.util.CrashHandler
import com.sakura.flowdrive.core.util.Logger
import com.tencent.mmkv.MMKV

class App : Application() {
    override fun attachBaseContext(base: Context) {
        MMKV.initialize(base)
        super.attachBaseContext(AppSettings.applyLocaleEarly(base))
    }

    override fun onCreate() {
        super.onCreate()
        AppSettings.init(this)
        Logger.initialize(this)
        CrashHandler.init(this)
    }
}
