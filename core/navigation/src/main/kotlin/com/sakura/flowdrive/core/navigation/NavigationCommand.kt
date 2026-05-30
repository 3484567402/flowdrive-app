package com.sakura.flowdrive.core.navigation

import androidx.navigation3.runtime.NavKey

internal sealed interface NavigationCommand {
    fun execute(controller: NavigationController)

    data class NavigateTo(
        val route: NavKey,
        val navOptions: NavigationOptions?,
    ) : NavigationCommand {
        override fun execute(controller: NavigationController) {
            controller.navigateTo(route, navOptions)
        }
    }

    data object NavigateUp : NavigationCommand {
        override fun execute(controller: NavigationController) {
            controller.navigateBack()
        }
    }

    data class NavigateBackTo(
        val route: NavKey,
        val inclusive: Boolean,
    ) : NavigationCommand {
        override fun execute(controller: NavigationController) {
            controller.navigateBackTo(route, inclusive)
        }
    }

    data class PopBackStackWithResult<T>(
        val key: NavigationResultKey<T>,
        val result: T,
    ) : NavigationCommand {
        override fun execute(controller: NavigationController) {
            controller.popBackStackWithResult(key, result)
        }
    }
}
