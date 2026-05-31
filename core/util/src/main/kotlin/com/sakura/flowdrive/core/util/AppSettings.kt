package com.sakura.flowdrive.core.util

import android.content.Context
import android.os.LocaleList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tencent.mmkv.MMKV
import java.util.Locale

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

    var languageCode by mutableStateOf("")
        private set

    val supportedLanguages = listOf(
        "" to "跟随系统",
        "zh" to "简体中文",
        "en" to "English",
        "ja" to "日本語",
        "ko" to "한국어",
        "fr" to "Français",
        "de" to "Deutsch",
        "es" to "Español",
    )

    fun init(context: Context) {
        MMKV.initialize(context)
        kv = MMKV.defaultMMKV()
        darkModeIndex = kv!!.decodeInt("dark_mode_index", 0)
        dynamicColor = kv!!.decodeBool("dynamic_color", true)
        isAmoled = kv!!.decodeBool("is_amoled", false)
        useSystemFont = kv!!.decodeBool("use_system_font", false)
        languageCode = kv!!.decodeString("language_code", "") ?: ""
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

    fun updateLanguage(code: String) {
        languageCode = code
        kv?.encode("language_code", code)
    }

    fun getLocale(): Locale {
        if (languageCode.isEmpty()) return Locale.getDefault()
        return Locale(languageCode)
    }

    fun applyLocale(context: Context, code: String = languageCode): Context {
        val locale = if (code.isEmpty()) Locale.getDefault() else Locale(code)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))
        return context.createConfigurationContext(config)
    }
}
