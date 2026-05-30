package com.sakura.flowdrive.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sakura.flowdrive.core.navigation.AppNavigator
import com.sakura.flowdrive.core.navigation.NavigationService
import com.sakura.flowdrive.core.navigation.createBackStackNavigationController
import com.sakura.flowdrive.core.navigation.main.MainRoutes
import com.sakura.flowdrive.feature.main.MainRoute
import com.sakura.flowdrive.feature.settings.navigation.SettingsRoutes
import com.sakura.flowdrive.feature.settings.navigation.settingsGraph

private const val NAV_ANIMATION_DURATION = 300

private val NAV_ANIMATION_SPEC: FiniteAnimationSpec<IntOffset> =
    tween(durationMillis = NAV_ANIMATION_DURATION)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    navigator: AppNavigator,
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(MainRoutes.Main)
    val navigationController = remember(backStack, navigator) {
        createBackStackNavigationController(backStack, navigator)
    }

    DisposableEffect(navigationController) {
        navigator.attachController(navigationController)
        NavigationService.bind(navigator)
        onDispose {
            NavigationService.unbind(navigator)
            navigator.detachController(navigationController)
        }
    }

    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            onBack = { navigationController.navigateBack() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            transitionSpec = { createForwardTransition() },
            popTransitionSpec = { createBackwardTransition() },
            predictivePopTransitionSpec = { createBackwardTransition() },
            entryProvider = appEntryProvider(navigationController, navigator),
        )
    }
}

private fun createForwardTransition() = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = NAV_ANIMATION_SPEC,
) togetherWith slideOutHorizontally(
    targetOffsetX = { -it },
    animationSpec = NAV_ANIMATION_SPEC,
)

private fun createBackwardTransition() = slideInHorizontally(
    initialOffsetX = { -it },
    animationSpec = NAV_ANIMATION_SPEC,
) togetherWith slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = NAV_ANIMATION_SPEC,
)

private fun appEntryProvider(
    navigationController: com.sakura.flowdrive.core.navigation.NavigationController,
    navigator: AppNavigator,
) = entryProvider {
    entry<MainRoutes.Main> {
        var currentPageIndex by remember { mutableIntStateOf(0) }
        MainRoute(
            currentPageIndex = currentPageIndex,
            onCurrentPageChanged = { currentPageIndex = it },
            onNavigateToSettings = { navigator.navigateTo(SettingsRoutes.Settings) },
        )
    }
    settingsGraph(navigationController, navigator)
}
