package com.sakura.flowdrive.feature.settings.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object SettingsRoutes {
    @Serializable
    data object Settings : NavKey

    @Serializable
    data object SubSetting1 : NavKey

    @Serializable
    data object SubSetting2 : NavKey
}
