package com.sakura.flowdrive.feature.main.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.CloudUpload
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

enum class TransferType { Upload, Download }
enum class TransferStatus { Active, Paused, Completed, Failed }

data class TransferTask(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: TransferType,
    val status: TransferStatus,
    val progress: Float,
    val sizeText: String,
    val speedText: String = "",
)

private val mockActiveTransfers = listOf(
    TransferTask(name = "项目资料.zip", type = TransferType.Upload, status = TransferStatus.Active, progress = 0.67f, sizeText = "234.5 MB / 350 MB", speedText = "2.3 MB/s"),
    TransferTask(name = "旅行照片合集.rar", type = TransferType.Upload, status = TransferStatus.Active, progress = 0.34f, sizeText = "89.2 MB / 260 MB", speedText = "1.8 MB/s"),
    TransferTask(name = "会议录屏_1020.mp4", type = TransferType.Upload, status = TransferStatus.Paused, progress = 0.12f, sizeText = "56.1 MB / 456 MB", speedText = ""),
    TransferTask(name = "Q4 工作计划.pdf", type = TransferType.Download, status = TransferStatus.Active, progress = 0.89f, sizeText = "3.6 MB / 4.1 MB", speedText = "5.1 MB/s"),
    TransferTask(name = "设计稿_v3.psd", type = TransferType.Download, status = TransferStatus.Active, progress = 0.45f, sizeText = "45.0 MB / 100 MB", speedText = "3.2 MB/s"),
    TransferTask(name = "数据库备份.sql", type = TransferType.Download, status = TransferStatus.Paused, progress = 0.08f, sizeText = "16.0 MB / 200 MB", speedText = ""),
)

private val mockCompletedTransfers = listOf(
    TransferTask(name = "年度总结.docx", type = TransferType.Upload, status = TransferStatus.Completed, progress = 1f, sizeText = "2.3 MB"),
    TransferTask(name = "IMG_8823.JPG", type = TransferType.Upload, status = TransferStatus.Completed, progress = 1f, sizeText = "2.4 MB"),
    TransferTask(name = "合同_甲方.pdf", type = TransferType.Download, status = TransferStatus.Completed, progress = 1f, sizeText = "1.8 MB"),
    TransferTask(name = "教程_Kotlin入门.mp4", type = TransferType.Download, status = TransferStatus.Completed, progress = 1f, sizeText = "125.6 MB"),
    TransferTask(name = "壁纸_01.JPG", type = TransferType.Upload, status = TransferStatus.Completed, progress = 1f, sizeText = "5.6 MB"),
    TransferTask(name = "简历_中文.pdf", type = TransferType.Download, status = TransferStatus.Completed, progress = 1f, sizeText = "856 KB"),
    TransferTask(name = "读书笔记_01.md", type = TransferType.Upload, status = TransferStatus.Completed, progress = 1f, sizeText = "12 KB"),
    TransferTask(name = "预算表_2024.xlsx", type = TransferType.Download, status = TransferStatus.Completed, progress = 1f, sizeText = "4.5 MB"),
    TransferTask(name = "扫描件_身份证.JPG", type = TransferType.Upload, status = TransferStatus.Completed, progress = 1f, sizeText = "3.2 MB"),
    TransferTask(name = "崩溃报告_1015.dmp", type = TransferType.Upload, status = TransferStatus.Failed, progress = 0.78f, sizeText = "78.0 MB / 100 MB"),
)

@Composable
fun TransferRoute() {
    TransferScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "传输列表",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("传输中 (${mockActiveTransfers.size})") },
                    leadingIcon = {
                        if (selectedTab == 0) {
                            Icon(
                                imageVector = Icons.Rounded.CloudUpload,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("已完成 (${mockCompletedTransfers.size})") },
                    leadingIcon = {
                        if (selectedTab == 1) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val tasks = if (selectedTab == 0) mockActiveTransfers else mockCompletedTransfers

            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = if (selectedTab == 0) "没有正在传输的任务" else "没有已完成的任务",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(tasks, key = { it.id }) { task ->
                        TransferTaskItem(task = task)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransferTaskItem(task: TransferTask) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor, shape)
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconVector: ImageVector = when (task.type) {
            TransferType.Upload -> Icons.Rounded.CloudUpload
            TransferType.Download -> Icons.Rounded.CloudDownload
        }
        val iconBackgroundColor = when (task.type) {
            TransferType.Upload -> Color(0xFFE8F0FE)
            TransferType.Download -> Color(0xFFE6F4EA)
        }
        val iconTintColor = when (task.type) {
            TransferType.Upload -> Color(0xFF1A73E8)
            TransferType.Download -> Color(0xFF1E8E3E)
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier.size(24.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))

            if (task.status == TransferStatus.Active || task.status == TransferStatus.Paused) {
                LinearProgressIndicator(
                    progress = { task.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    strokeCap = StrokeCap.Round,
                    color = if (task.status == TransferStatus.Paused) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = task.sizeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (task.speedText.isNotEmpty()) {
                        Text(
                            text = task.speedText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            } else {
                Text(
                    text = task.sizeText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        when (task.status) {
            TransferStatus.Active -> {
                CircularProgressIndicator(
                    progress = { task.progress },
                    modifier = Modifier.size(28.dp),
                    strokeWidth = 2.5.dp,
                    strokeCap = StrokeCap.Round,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            TransferStatus.Paused -> {
                Icon(
                    imageVector = Icons.Rounded.Pause,
                    contentDescription = "Paused",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(24.dp),
                )
            }
            TransferStatus.Completed -> {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF1E8E3E),
                    modifier = Modifier.size(24.dp),
                )
            }
            TransferStatus.Failed -> {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = "Failed",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

