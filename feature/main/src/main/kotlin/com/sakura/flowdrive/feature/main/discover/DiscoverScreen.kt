package com.sakura.flowdrive.feature.main.discover

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
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FolderShared
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class QuickAction(
    val labelResId: Int,
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconTintColor: Color,
)

data class SharedFile(
    val name: String,
    val sharer: String,
    val date: String,
    val typeIcon: ImageVector,
    val typeColor: Color,
)

data class HotCategory(
    val nameResId: Int,
    val count: String,
    val icon: ImageVector,
    val color: Color,
)

private val quickActions = listOf(
    QuickAction(R.string.recent_files, Icons.Rounded.History, Color(0xFFE8F0FE), Color(0xFF1A73E8)),
    QuickAction(R.string.favorite_files, Icons.Rounded.Star, Color(0xFFFEF7E0), Color(0xFFF9AB00)),
    QuickAction(R.string.shared_space, Icons.Rounded.FolderShared, Color(0xFFE6F4EA), Color(0xFF1E8E3E)),
    QuickAction(R.string.hot_recommend, Icons.Rounded.LocalFireDepartment, Color(0xFFFCE8E6), Color(0xFFD93025)),
)

private val sharedFiles = listOf(
    SharedFile("Q3 季度汇报.pdf", "张三", "10分钟前", Icons.Rounded.Star, Color(0xFFF9AB00)),
    SharedFile("设计规范_v2.pdf", "李四", "1小时前", Icons.Rounded.Star, Color(0xFFF9AB00)),
    SharedFile("项目源码.zip", "王五", "3小时前", Icons.Rounded.Star, Color(0xFFF9AB00)),
    SharedFile("会议纪要_1020.docx", "赵六", "昨天", Icons.Rounded.Star, Color(0xFFF9AB00)),
    SharedFile("产品原型.fig", "孙七", "昨天", Icons.Rounded.Star, Color(0xFFF9AB00)),
    SharedFile("测试报告.xlsx", "周八", "2天前", Icons.Rounded.Star, Color(0xFFF9AB00)),
)

private val hotCategories = listOf(
    HotCategory(R.string.category_work, "1,234 个", Icons.AutoMirrored.Rounded.TrendingUp, Color(0xFF1A73E8)),
    HotCategory(R.string.category_study, "892 个", Icons.AutoMirrored.Rounded.TrendingUp, Color(0xFF1E8E3E)),
    HotCategory(R.string.category_media, "567 个", Icons.AutoMirrored.Rounded.TrendingUp, Color(0xFFD93025)),
    HotCategory(R.string.category_music, "345 个", Icons.AutoMirrored.Rounded.TrendingUp, Color(0xFFF9AB00)),
)

@Composable
fun DiscoverRoute() {
    DiscoverScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.discover),
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = { isActive = false },
                            expanded = isActive,
                            onExpandedChange = { isActive = it },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_files),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            },
                        )
                    },
                    expanded = isActive,
                    onExpandedChange = { isActive = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    ),
                ) {}
            }

            item {
                Column {
                    Text(
                        text = stringResource(R.string.quick_access),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        quickActions.forEach { action ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { },
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(action.backgroundColor),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Icon(
                                        imageVector = action.icon,
                                        contentDescription = stringResource(action.labelResId),
                                        tint = action.iconTintColor,
                                        modifier = Modifier.size(26.dp),
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stringResource(action.labelResId),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column {
                    Text(
                        text = stringResource(R.string.hot_categories),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        hotCategories.forEach { category ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                    .clickable { }
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    tint = category.color,
                                    modifier = Modifier.size(24.dp),
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = stringResource(category.nameResId),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = category.count,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.shared_updates),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(R.string.view_all),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { },
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(sharedFiles) { file ->
                SharedFileItem(file = file)
            }
        }
    }
}

@Composable
private fun SharedFileItem(file: SharedFile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFEF7E0)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = file.typeIcon,
                contentDescription = null,
                tint = file.typeColor,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${file.sharer} 分享 · ${file.date}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier
                .size(20.dp)
                .clickable { },
        )
    }
}
