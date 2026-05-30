package com.sakura.flowdrive.core.navigation.main

import androidx.navigation3.runtime.NavKey
import com.sakura.flowdrive.core.navigation.navigate
import com.sakura.flowdrive.core.navigation.navigateAndCloseCurrent

object MainNavigator {
    fun toMain() {
        navigate(MainRoutes.Main)
    }

    fun toMainAndCloseCurrent(currentRoute: NavKey) {
        navigateAndCloseCurrent(
            route = MainRoutes.Main,
            currentRoute = currentRoute,
        )
    }

    fun toHome() {
        navigate(MainRoutes.Home)
    }

    fun toFiles() {
        navigate(MainRoutes.Files)
    }

    fun toDiscover() {
        navigate(MainRoutes.Discover)
    }

    fun toMine() {
        navigate(MainRoutes.Mine)
    }
}
