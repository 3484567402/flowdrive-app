package com.sakura.flowdrive.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class AppNavigator(
    private val appState: AppState,
    private val routeInterceptor: RouteInterceptor = RouteInterceptor { false },
) {
    private val lock = Any()

    private var controller: NavigationController? = null

    private val pendingCommands = ArrayDeque<NavigationCommand>()

    private val _resultEvents = MutableSharedFlow<ResultEvent>(extraBufferCapacity = 32)

    private var _loginRouteProvider: (() -> NavKey)? = null

    fun setLoginRouteProvider(provider: () -> NavKey) {
        _loginRouteProvider = provider
    }

    fun attachController(navigationController: NavigationController) {
        synchronized(lock) {
            controller = navigationController
            while (pendingCommands.isNotEmpty()) {
                pendingCommands.removeFirst().execute(navigationController)
            }
        }
    }

    fun detachController(navigationController: NavigationController) {
        synchronized(lock) {
            if (controller === navigationController) {
                controller = null
            }
        }
    }

    fun navigateTo(route: NavKey, navOptions: NavigationOptions? = null) {
        val targetRoute = resolveTargetRoute(route)
        executeOrEnqueue(NavigationCommand.NavigateTo(targetRoute, navOptions))
    }

    fun navigateBack() {
        executeOrEnqueue(NavigationCommand.NavigateUp)
    }

    fun <T> popBackStackWithResult(key: NavigationResultKey<T>, result: T) {
        executeOrEnqueue(NavigationCommand.PopBackStackWithResult(key, result))
    }

    fun navigateBackTo(route: NavKey, inclusive: Boolean = false) {
        executeOrEnqueue(NavigationCommand.NavigateBackTo(route, inclusive))
    }

    fun <T> resultEvents(key: NavigationResultKey<T>): Flow<T> {
        return _resultEvents
            .filter { it.key == key.key }
            .map { key.deserialize(it.rawValue) }
    }

    internal fun <T> dispatchResult(key: NavigationResultKey<T>, result: T) {
        val rawValue = key.serialize(result)
        _resultEvents.tryEmit(ResultEvent(key = key.key, rawValue = rawValue))
    }

    private fun executeOrEnqueue(command: NavigationCommand) {
        synchronized(lock) {
            val currentController = controller
            if (currentController != null) {
                command.execute(currentController)
            } else {
                pendingCommands.addLast(command)
            }
        }
    }

    private fun resolveTargetRoute(route: NavKey): NavKey {
        return if (routeInterceptor.requiresLogin(route) && !appState.isLoggedIn.value) {
            _loginRouteProvider?.invoke() ?: route
        } else {
            route
        }
    }
}

private data class ResultEvent(
    val key: String,
    val rawValue: Any,
)
