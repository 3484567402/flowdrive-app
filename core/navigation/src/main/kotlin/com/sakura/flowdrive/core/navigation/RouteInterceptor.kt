package com.sakura.flowdrive.core.navigation

import androidx.navigation3.runtime.NavKey

fun interface RouteInterceptor {
    fun requiresLogin(route: NavKey): Boolean
}
