package com.sakura.flowdrive.feature.settings.navigation

import com.sakura.flowdrive.core.navigation.navigate

object SettingsNavigator {
    fun toSettings() {
        navigate(SettingsRoutes.Settings)
    }

    fun toSubSetting1() {
        navigate(SettingsRoutes.SubSetting1)
    }

    fun toSubSetting2() {
        navigate(SettingsRoutes.SubSetting2)
    }
}
