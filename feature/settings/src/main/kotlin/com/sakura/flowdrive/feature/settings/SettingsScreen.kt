package com.sakura.flowdrive.feature.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsRoute(
    onNavigateBack: () -> Unit = {},
    onNavigateToSubSetting1: () -> Unit = {},
    onNavigateToSubSetting2: () -> Unit = {},
    onNavigateToLanguage: () -> Unit = {},
) {
    SettingsScreen(
        onBack = onNavigateBack,
        onSubSetting1 = onNavigateToSubSetting1,
        onSubSetting2 = onNavigateToSubSetting2,
        onLanguage = onNavigateToLanguage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit = {},
    onSubSetting1: () -> Unit = {},
    onSubSetting2: () -> Unit = {},
    onLanguage: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings), fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCategory(title = stringResource(R.string.settings_account))
            }
            item {
                ModernSettingItem(
                    icon = Icons.Default.Person,
                    title = stringResource(R.string.settings_account_security),
                    subtitle = stringResource(R.string.settings_account_security_desc),
                    onClick = onSubSetting1,
                )
            }
            item {
                ModernSettingItem(
                    icon = Icons.Default.SwitchAccount,
                    title = stringResource(R.string.settings_switch_account),
                    subtitle = stringResource(R.string.settings_switch_account_desc),
                    onClick = onSubSetting2,
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingsCategory(title = stringResource(R.string.settings_appearance))
            }
            item { DarkModeSettingItem() }
            item { DynamicColorSettingItem() }
            item { FontSettingItem() }
            item { LanguageSettingItem(onClick = onLanguage) }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingsCategory(title = stringResource(R.string.settings_app))
            }
            item {
                ModernSettingItem(
                    icon = Icons.Default.Delete,
                    title = stringResource(R.string.settings_clear_cache),
                    subtitle = stringResource(R.string.settings_clear_cache_desc),
                    onClick = { },
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                SettingsCategory(title = stringResource(R.string.settings_support_about))
            }
            item {
                ModernSettingItem(
                    icon = Icons.Default.Feedback,
                    title = stringResource(R.string.settings_feedback),
                    subtitle = stringResource(R.string.settings_feedback_desc),
                    onClick = { },
                )
            }
            item {
                ModernSettingItem(
                    icon = Icons.Default.Info,
                    title = stringResource(R.string.settings_about),
                    subtitle = stringResource(R.string.settings_about_desc),
                    onClick = { },
                )
            }
            item {
                ModernSettingItem(
                    icon = Icons.Default.Code,
                    title = stringResource(R.string.settings_opensource),
                    subtitle = stringResource(R.string.settings_opensource_desc),
                    onClick = { },
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(0.85f).height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                    ) {
                        Text(text = stringResource(R.string.settings_logout), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.settings_copyright),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
        }
    }
}
