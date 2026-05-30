package com.sakura.flowdrive.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.Flow

object NavigationService {
    @Volatile
    private var navigator: AppNavigator? = null

    fun bind(appNavigator: AppNavigator) {
        navigator = appNavigator
    }

    fun unbind(appNavigator: AppNavigator) {
        if (navigator === appNavigator) {
            navigator = null
        }
    }

    fun requireNavigator(): AppNavigator {
        return navigator ?: error("AppNavigator is not bound")
    }

    fun navigate(route: NavKey, navOptions: NavigationOptions? = null) {
        requireNavigator().navigateTo(route = route, navOptions = navOptions)
    }

    fun navigateAndCloseCurrent(route: NavKey, currentRoute: NavKey) {
        val navOptions = NavigationOptions(
            popUpToRoute = currentRoute,
            inclusive = true,
            allowPopToEmpty = true,
        )
        requireNavigator().navigateTo(route = route, navOptions = navOptions)
    }

    fun navigateWithPopUpTo(route: NavKey, popUpToRoute: NavKey, inclusive: Boolean = false) {
        val navOptions = NavigationOptions(
            popUpToRoute = popUpToRoute,
            inclusive = inclusive,
        )
        requireNavigator().navigateTo(route = route, navOptions = navOptions)
    }

    fun navigateBack() {
        requireNavigator().navigateBack()
    }

    fun navigateBackTo(route: NavKey, inclusive: Boolean = false) {
        requireNavigator().navigateBackTo(route = route, inclusive = inclusive)
    }

    fun <T> popBackStackWithResult(key: NavigationResultKey<T>, result: T) {
        requireNavigator().popBackStackWithResult(key = key, result = result)
    }

    fun <T> navigateBackWithResult(key: NavigationResultKey<T>, result: T) {
        popBackStackWithResult(key = key, result = result)
    }

    fun <T> resultEvents(key: NavigationResultKey<T>): Flow<T> {
        return requireNavigator().resultEvents(key)
    }
}

fun navigate(route: NavKey, navOptions: NavigationOptions? = null) {
    NavigationService.navigate(route = route, navOptions = navOptions)
}

fun navigateAndCloseCurrent(route: NavKey, currentRoute: NavKey) {
    NavigationService.navigateAndCloseCurrent(route = route, currentRoute = currentRoute)
}

fun navigateWithPopUpTo(route: NavKey, popUpToRoute: NavKey, inclusive: Boolean = false) {
    NavigationService.navigateWithPopUpTo(
        route = route,
        popUpToRoute = popUpToRoute,
        inclusive = inclusive,
    )
}

fun navigateBack() {
    NavigationService.navigateBack()
}

fun navigateBackTo(route: NavKey, inclusive: Boolean = false) {
    NavigationService.navigateBackTo(route = route, inclusive = inclusive)
}

fun <T> popBackStackWithResult(key: NavigationResultKey<T>, result: T) {
    NavigationService.popBackStackWithResult(key = key, result = result)
}

fun <T> navigateBackWithResult(key: NavigationResultKey<T>, result: T) {
    NavigationService.navigateBackWithResult(key = key, result = result)
}

fun <T> resultEvents(key: NavigationResultKey<T>): Flow<T> {
    return NavigationService.resultEvents(key)
}
