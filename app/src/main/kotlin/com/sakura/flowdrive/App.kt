package com.sakura.flowdrive

import android.app.Application
import com.sakura.flowdrive.core.utils.CrashHandler
import com.sakura.flowdrive.core.utils.Logger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.initialize(this)
        CrashHandler.init(this)
    }
}
