package com.sakura.flowdrive.core.navigation

import androidx.navigation3.runtime.NavKey

interface NavigationController {
    fun navigateTo(route: NavKey, navOptions: NavigationOptions?)

    fun navigateBack()

    fun navigateBackTo(route: NavKey, inclusive: Boolean)

    fun <T> popBackStackWithResult(key: NavigationResultKey<T>, result: T)
}
