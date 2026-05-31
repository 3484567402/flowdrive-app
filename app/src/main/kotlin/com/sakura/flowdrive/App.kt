package com.sakura.flowdrive

import android.app.Application
import android.content.Context
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.core.util.CrashHandler
import com.sakura.flowdrive.core.util.Logger
import com.tencent.mmkv.MMKV

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppSettings.init(this)
        Logger.initialize(this)
        CrashHandler.init(this) { context, crashInfo ->
            CrashActivity.start(context, crashInfo)
        }
    }

    override fun attachBaseContext(base: Context) {
        MMKV.initialize(base)
        val kv = MMKV.defaultMMKV()
        val savedLang = kv.decodeString("language_code", "") ?: ""
        val context = if (savedLang.isNotEmpty()) {
            AppSettings.applyLocale(base, savedLang)
        } else {
            base
        }
        super.attachBaseContext(context)
    }
}
