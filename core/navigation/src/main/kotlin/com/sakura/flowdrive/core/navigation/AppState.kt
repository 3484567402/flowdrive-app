package com.sakura.flowdrive.core.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

interface AppState {
    val isLoggedIn: MutableStateFlow<Boolean>
}
