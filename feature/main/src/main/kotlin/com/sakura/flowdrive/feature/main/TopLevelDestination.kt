package com.sakura.flowdrive.feature.main

import androidx.annotation.StringRes
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
    @StringRes val labelResId: Int,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
) {
    HOME(
        labelResId = R.string.home,
        filledIcon = Icons.Filled.Folder,
        outlinedIcon = Icons.Outlined.Folder,
    ),
    TRANSFER(
        labelResId = R.string.transfer,
        filledIcon = Icons.Filled.CloudUpload,
        outlinedIcon = Icons.Outlined.CloudUpload,
    ),
    DISCOVER(
        labelResId = R.string.discover,
        filledIcon = Icons.Filled.Explore,
        outlinedIcon = Icons.Outlined.Explore,
    ),
    ME(
        labelResId = R.string.me,
        filledIcon = Icons.Filled.Person,
        outlinedIcon = Icons.Outlined.Person,
    ),
}
