package com.sakura.flowdrive.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun createBackStackNavigationController(
    backStack: NavBackStack<NavKey>,
    navigator: AppNavigator,
): NavigationController {
    return BackStackNavigationController(backStack = backStack, navigator = navigator)
}

private class BackStackNavigationController(
    private val backStack: NavBackStack<NavKey>,
    private val navigator: AppNavigator,
) : NavigationController {

    override fun navigateTo(route: NavKey, navOptions: NavigationOptions?) {
        val popUpToRoute = navOptions?.popUpToRoute
        if (popUpToRoute != null) {
            backStack.popUpTo(
                route = popUpToRoute,
                inclusive = navOptions.inclusive,
                allowPopToEmpty = navOptions.allowPopToEmpty,
            )
        }
        backStack.add(route)
    }

    override fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    override fun navigateBackTo(route: NavKey, inclusive: Boolean) {
        backStack.popUpTo(route = route, inclusive = inclusive)
    }

    override fun <T> popBackStackWithResult(key: NavigationResultKey<T>, result: T) {
        navigator.dispatchResult(key = key, result = result)
        navigateBack()
    }
}

private fun NavBackStack<NavKey>.popUpTo(
    route: NavKey,
    inclusive: Boolean,
    allowPopToEmpty: Boolean = false,
) {
    val targetIndex = indexOfLast { it == route }
    if (targetIndex == -1) return

    val removeFromIndex = if (inclusive) targetIndex else targetIndex + 1
    if (removeFromIndex >= size) return

    if (removeFromIndex == 0) {
        if (allowPopToEmpty) {
            clear()
        } else if (size > 1) {
            subList(1, size).clear()
        }
        return
    }

    subList(removeFromIndex, size).clear()
}
