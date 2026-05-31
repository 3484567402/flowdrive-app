package com.sakura.flowdrive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sakura.flowdrive.core.navigation.AppNavigator
import com.sakura.flowdrive.core.navigation.AppState
import com.sakura.flowdrive.core.util.AppSettings
import com.sakura.flowdrive.navigation.AppNavHost
import com.sakura.flowdrive.ui.theme.FlowDriveTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    private val appState = object : AppState {
        override val isLoggedIn = MutableStateFlow(false)
    }

    private val navigator = AppNavigator(appState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppSettings.init(this)
        enableEdgeToEdge()
        setContent {
            FlowDriveTheme(
                dynamicColor = AppSettings.dynamicColor,
            ) {
                AppNavHost(navigator = navigator)
            }
        }
    }
}
