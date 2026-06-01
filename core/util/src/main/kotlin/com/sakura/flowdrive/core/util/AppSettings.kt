package com.sakura.flowdrive.core.util

import android.content.Context
import android.os.LocaleList
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tencent.mmkv.MMKV
import java.util.Locale

object AppSettings {

    private lateinit var kv: MMKV

    private const val KEY_DARK_MODE_INDEX = "dark_mode_index"
    private const val KEY_DYNAMIC_COLOR = "dynamic_color"
    private const val KEY_IS_AMOLED = "is_amoled"
    private const val KEY_USE_SYSTEM_FONT = "use_system_font"
    private const val KEY_LANGUAGE_CODE = "language_code"

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

    data class LanguageOption(val code: String, val displayName: String)

    val supportedLanguages = listOf(
        LanguageOption("", "跟随系统"),
        LanguageOption("zh", "简体中文"),
        LanguageOption("en", "English"),
    )

    fun initMMKV(context: Context) {
        MMKV.initialize(context)
    }

    fun init() {
        kv = MMKV.defaultMMKV()
        darkModeIndex = kv.decodeInt(KEY_DARK_MODE_INDEX, 0)
        dynamicColor = kv.decodeBool(KEY_DYNAMIC_COLOR, true)
        isAmoled = kv.decodeBool(KEY_IS_AMOLED, false)
        useSystemFont = kv.decodeBool(KEY_USE_SYSTEM_FONT, false)
        languageCode = kv.decodeString(KEY_LANGUAGE_CODE, "") ?: ""
    }

    fun updateDarkMode(index: Int) {
        darkModeIndex = index
        kv.encode(KEY_DARK_MODE_INDEX, index)
    }

    fun updateDynamicColor(enabled: Boolean) {
        dynamicColor = enabled
        kv.encode(KEY_DYNAMIC_COLOR, enabled)
    }

    fun updateAmoled(enabled: Boolean) {
        isAmoled = enabled
        kv.encode(KEY_IS_AMOLED, enabled)
    }

    fun updateUseSystemFont(enabled: Boolean) {
        useSystemFont = enabled
        kv.encode(KEY_USE_SYSTEM_FONT, enabled)
    }

    fun updateLanguage(code: String) {
        languageCode = code
        kv.encode(KEY_LANGUAGE_CODE, code)
    }

    fun isDarkMode(): Boolean = kv.decodeInt(KEY_DARK_MODE_INDEX, 0) == 2

    fun isDynamicColorEnabled(): Boolean = kv.decodeBool(KEY_DYNAMIC_COLOR, true)

    fun isAmoledEnabled(): Boolean = kv.decodeBool(KEY_IS_AMOLED, false)

    fun isSystemFontEnabled(): Boolean = kv.decodeBool(KEY_USE_SYSTEM_FONT, false)

    fun getLanguageCode(): String = kv.decodeString(KEY_LANGUAGE_CODE, "") ?: ""

    private fun buildLocale(code: String): Locale {
        return Locale.Builder().setLanguageTag(code).build()
    }

    fun getLocale(): Locale {
        val code = getLanguageCode()
        return if (code.isEmpty()) Locale.getDefault() else buildLocale(code)
    }

    fun applyLocale(context: Context, code: String = languageCode): Context {
        val locale = if (code.isEmpty()) Locale.getDefault() else buildLocale(code)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLocales(LocaleList(locale))
        return context.createConfigurationContext(config)
    }

    fun applyLocaleEarly(context: Context): Context {
        return if (languageCode.isNotEmpty()) {
            applyLocale(context)
        } else {
            context
        }
    }
}
