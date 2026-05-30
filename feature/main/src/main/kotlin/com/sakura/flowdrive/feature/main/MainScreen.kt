package com.sakura.flowdrive.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sakura.flowdrive.feature.main.discover.DiscoverRoute
import com.sakura.flowdrive.feature.main.files.FilesRoute
import com.sakura.flowdrive.feature.main.me.MeRoute
import com.sakura.flowdrive.feature.main.transfer.TransferRoute
import kotlinx.coroutines.launch

@Composable
fun MainRoute(
    currentPageIndex: Int,
    onCurrentPageChanged: (Int) -> Unit,
    onNavigateToSettings: () -> Unit = {},
) {
    MainScreen(
        currentPageIndex = currentPageIndex,
        onCurrentPageChanged = onCurrentPageChanged,
        onNavigateToSettings = onNavigateToSettings,
    )
}

@Composable
fun MainScreen(
    currentPageIndex: Int = 0,
    onCurrentPageChanged: (Int) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(initialPage = currentPageIndex) {
        TopLevelDestination.entries.size
    }

    LaunchedEffect(pageState.currentPage) {
        onCurrentPageChanged(pageState.currentPage)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            when (page) {
                0 -> FilesRoute()
                1 -> TransferRoute()
                2 -> DiscoverRoute()
                3 -> MeRoute(
                    onNavigateToSettings = onNavigateToSettings,
                )
            }
        }

        BottomNavigationBar(
            destinations = TopLevelDestination.entries,
            onNavigateToDestination = { index ->
                onCurrentPageChanged(index)
                scope.launch { pageState.scrollToPage(index) }
            },
            currentPageIndex = currentPageIndex,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
