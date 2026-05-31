package com.sakura.flowdrive.core.util

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tencent.mmkv.MMKV

object AppSettings {

    private var kv: MMKV? = null

    var darkModeIndex by mutableStateOf(0)
        private set

    var dynamicColor by mutableStateOf(true)
        private set

    var isAmoled by mutableStateOf(false)
        private set

    var useSystemFont by mutableStateOf(false)
        private set

    fun init(context: Context) {
        MMKV.initialize(context)
        kv = MMKV.defaultMMKV()
        darkModeIndex = kv!!.decodeInt("dark_mode_index", 0)
        dynamicColor = kv!!.decodeBool("dynamic_color", true)
        isAmoled = kv!!.decodeBool("is_amoled", false)
        useSystemFont = kv!!.decodeBool("use_system_font", false)
    }

    fun updateDarkMode(index: Int) {
        darkModeIndex = index
        kv?.encode("dark_mode_index", index)
    }

    fun updateDynamicColor(enabled: Boolean) {
        dynamicColor = enabled
        kv?.encode("dynamic_color", enabled)
    }

    fun updateAmoled(enabled: Boolean) {
        isAmoled = enabled
        kv?.encode("is_amoled", enabled)
    }

    fun updateUseSystemFont(enabled: Boolean) {
        useSystemFont = enabled
        kv?.encode("use_system_font", enabled)
    }
}
