package com.sakura.flowdrive.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.sakura.flowdrive.core.navigation.AppNavigator
import com.sakura.flowdrive.core.navigation.NavigationController
import com.sakura.flowdrive.feature.settings.LanguageSettingRoute
import com.sakura.flowdrive.feature.settings.SettingsRoute
import com.sakura.flowdrive.feature.settings.SubSetting1Route
import com.sakura.flowdrive.feature.settings.SubSetting2Route

fun EntryProviderScope<NavKey>.settingsGraph(
    navigationController: NavigationController,
    navigator: AppNavigator,
) {
    entry<SettingsRoutes.Settings> {
        SettingsRoute(
            onNavigateBack = { navigationController.navigateBack() },
            onNavigateToSubSetting1 = { navigator.navigateTo(SettingsRoutes.SubSetting1) },
            onNavigateToSubSetting2 = { navigator.navigateTo(SettingsRoutes.SubSetting2) },
            onNavigateToLanguage = { navigator.navigateTo(SettingsRoutes.LanguageSetting) },
        )
    }
    entry<SettingsRoutes.SubSetting1> {
        SubSetting1Route(
            onNavigateBack = { navigationController.navigateBack() },
        )
    }
    entry<SettingsRoutes.SubSetting2> {
        SubSetting2Route(
            onNavigateBack = { navigationController.navigateBack() },
        )
    }
    entry<SettingsRoutes.LanguageSetting> {
        LanguageSettingRoute(
            onNavigateBack = { navigationController.navigateBack() },
        )
    }
}
