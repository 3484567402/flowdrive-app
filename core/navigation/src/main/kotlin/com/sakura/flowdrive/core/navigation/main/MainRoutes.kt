package com.sakura.flowdrive.core.navigation.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object MainRoutes {
    @Serializable
    data object Main : NavKey

    @Serializable
    data object Home : NavKey

    @Serializable
    data object Files : NavKey

    @Serializable
    data object Discover : NavKey

    @Serializable
    data object Mine : NavKey
}
