package com.sakura.quantumplanet.core.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakura.quantumplanet.core.utils.Logger
import com.sakura.quantumplanet.ui.theme.QuantumPlanetTheme
import com.sakura.quantumplanet.feature.settings.ThemeConfig
import kotlin.system.exitProcess

class CrashActivity : ComponentActivity() {

    companion object {
        fun start(context: Context, crashInfo: String) {
            val intent = Intent(context, CrashActivity::class.java).apply {
                putExtra("crash_log", crashInfo)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        val crashLog = intent.getStringExtra("crash_log") ?: "未获取到日志内容"

        Logger.e("CRASH_RESCUE", "救援进程启动成功，展示崩溃信息")

        setContent {
            QuantumPlanetTheme(dynamicColor = ThemeConfig.isDynamicColor) {
                CrashScreen(crashLog)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CrashScreen(log: String) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("系统响应异常", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                )
            },
            bottomBar = {
                BottomActionArea(log)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("应用捕获到严重错误，日志已存至本地。", fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    color = Color(0xFF1E1E1E), 
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.padding(12.dp).verticalScroll(rememberScrollState())) {
                        Text(
                            text = log,
                            color = Color(0xFFCE9178), 
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BottomActionArea(log: String) {
        val context = LocalContext.current
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.navigationBarsPadding().padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cm.setPrimaryClip(ClipData.newPlainText("CrashLog", log))
                        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ContentCopy, null)
                    Spacer(Modifier.width(4.dp))
                    Text("复制")
                }

                Button(
                    onClick = {
                        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)
                        exitProcess(0)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(4.dp))
                    Text("重启应用")
                }
            }
        }
    }
}