package com.sakura.flowdrive.feature.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.sakura.flowdrive.core.navigation.AppNavigator
import com.sakura.flowdrive.core.navigation.NavigationController
import com.sakura.flowdrive.feature.settings.SettingsRoute
import com.sakura.flowdrive.feature.settings.SubSetting1Route
import com.sakura.flowdrive.feature.settings.SubSetting2Route

fun EntryProviderScope<NavKey>.settingsGraph(
    navigationController: NavigationController,
    navigator: AppNavigator,
) {
    entry<SettingsRoutes.Settings> {
        SettingsRoute(
            onBack = { navigationController.navigateBack() },
            onAbout = { },
            onOpenSource = { },
            onEditInfo = { },
            onStorMgmt = { },
            onLogin = { },
            onPlayerSettings = { },
            onDownloadManager = { },
            onUISettings = { },
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
}
