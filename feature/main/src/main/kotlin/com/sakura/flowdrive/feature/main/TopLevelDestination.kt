package com.sakura.flowdrive.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
) {
    HOME(
        label = "首页",
        filledIcon = Icons.Filled.Folder,
        outlinedIcon = Icons.Outlined.Folder,
    ),
    TRANSFER(
        label = "传输",
        filledIcon = Icons.Filled.CloudUpload,
        outlinedIcon = Icons.Outlined.CloudUpload,
    ),
    DISCOVER(
        label = "发现",
        filledIcon = Icons.Filled.Explore,
        outlinedIcon = Icons.Outlined.Explore,
    ),
    ME(
        label = "我的",
        filledIcon = Icons.Filled.Person,
        outlinedIcon = Icons.Outlined.Person,
    ),
}
