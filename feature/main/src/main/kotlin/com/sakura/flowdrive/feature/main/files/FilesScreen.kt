package com.sakura.flowdrive.feature.main.files

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.automirrored.rounded.ViewList
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.util.UUID

enum class FileType {
    Folder, Image, Video, Document, Unknown
}

data class FileItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: FileType,
    val date: String,
    val size: String
)

private val folderNames = listOf(
    "DCIM 照片", "工作文档", "下载", "音乐", "视频", "截图",
    "微信文件", "QQ文件", "蓝牙传输", "备份", "项目资料", "学习笔记",
)

private val imageNames = listOf(
    "IMG_8823.JPG", "IMG_8824.JPG", "IMG_8825.HEIC", "IMG_8826.PNG",
    "风景照_01.JPG", "风景照_02.JPG", "自拍_20231020.JPG", "自拍_20231021.HEIC",
    "美食_火锅.JPG", "美食_寿司.JPG", "美食_烧烤.JPG", "美食_咖啡.JPG",
    "宠物_猫.JPG", "宠物_狗.JPG", "宠物_仓鼠.HEIC", "宠物_鹦鹉.JPG",
    "旅行_北京_01.JPG", "旅行_北京_02.JPG", "旅行_上海_01.PNG", "旅行_上海_02.JPG",
    "证件照_正面.JPG", "证件照_侧面.JPG", "截图_20231020.PNG", "截图_20231021.PNG",
    "壁纸_01.JPG", "壁纸_02.JPG", "壁纸_03.HEIC", "壁纸_04.JPG",
    "头像_旧.JPG", "头像_新.PNG", "相册_毕业.JPG", "相册_聚会.JPG",
    "相册_生日.JPG", "相册_春游.HEIC", "相册_年会.JPG", "相册_团建.JPG",
    "扫描件_身份证.JPG", "扫描件_护照.JPG", "扫描件_驾照.PNG", "扫描件_银行卡.JPG",
)

private val videoNames = listOf(
    "旅行Vlog.mp4", "生日聚会.mp4", "毕业典礼.mp4", "年会表演.mp4",
    "产品演示_01.mp4", "产品演示_02.mp4", "会议录屏_1015.mp4", "会议录屏_1020.mp4",
    "教程_Kotlin入门.mp4", "教程_Compose进阶.mp4", "教程_Gradle配置.mp4", "教程_Git使用.mp4",
    "短视频_01.mp4", "短视频_02.mp4", "短视频_03.mp4", "短视频_04.mp4",
    "监控_前门_20231020.mp4", "监控_后门_20231020.mp4", "监控_车库_20231020.mp4", "监控_花园_20231020.mp4",
    "电影_片段_01.mkv", "电影_片段_02.mkv", "纪录片_01.mp4", "纪录片_02.mp4",
    "婚礼_上午.mp4", "婚礼_下午.mp4", "宝宝_第一次走路.mp4", "宝宝_第一次说话.mp4",
    "演唱会_01.mp4", "演唱会_02.mp4", "运动_跑步.mp4", "运动_骑行.mp4",
    "延时摄影_日落.mp4", "延时摄影_云海.mp4", "慢动作_水滴.mp4", "慢动作_烟花.mp4",
    "直播回放_1015.mp4", "直播回放_1016.mp4", "直播回放_1017.mp4", "直播回放_1018.mp4",
)

private val documentNames = listOf(
    "Q3 季度汇报.pdf", "Q4 工作计划.pdf", "年度总结.docx", "项目方案_v2.docx",
    "会议纪要_1015.docx", "会议纪要_1020.docx", "需求文档_v1.0.pdf", "需求文档_v2.1.pdf",
    "技术方案_前端.docx", "技术方案_后端.docx", "API文档_v3.pdf", "数据库设计.docx",
    "合同_甲方.pdf", "合同_乙方.pdf", "发票_10月.pdf", "报销单_Q3.xlsx",
    "预算表_2024.xlsx", "考勤表_10月.xlsx", "工资条_9月.pdf", "工资条_10月.pdf",
    "简历_中文.pdf", "简历_英文.pdf", "求职信.docx", "推荐信.pdf",
    "论文_初稿.docx", "论文_终稿.pdf", "开题报告.pdf", "答辩PPT.pptx",
    "读书笔记_01.md", "读书笔记_02.md", "学习计划.xlsx", "课程表.xlsx",
    "租房合同.pdf", "购房合同.pdf", "保险单.pdf", "体检报告.pdf",
    "密码备忘.txt", "地址簿.csv", "待办事项.txt", "日记_20231020.txt",
)

private val unknownNames = listOf(
    "未知文件.dat", "缓存_01.tmp", "缓存_02.tmp", "系统日志_01.log",
    "系统日志_02.log", "配置_backup.cfg", "数据_dump.bin", "临时文件_01.tmp",
    "临时文件_02.tmp", "调试信息.dbg", "崩溃报告_1015.dmp", "崩溃报告_1020.dmp",
)

private val imageSizes = listOf(
    "2.4 MB", "1.8 MB", "3.2 MB", "4.1 MB", "5.6 MB", "1.2 MB",
    "2.8 MB", "3.7 MB", "900 KB", "1.5 MB", "6.3 MB", "2.1 MB",
)

private val videoSizes = listOf(
    "125.6 MB", "89.3 MB", "1.2 GB", "456.7 MB", "234.5 MB", "67.8 MB",
    "312.4 MB", "78.9 MB", "543.2 MB", "156.7 MB", "98.4 MB", "267.1 MB",
)

private val documentSizes = listOf(
    "4.2 MB", "1.8 MB", "856 KB", "2.3 MB", "5.1 MB", "3.7 MB",
    "12.4 MB", "678 KB", "1.1 MB", "9.3 MB", "2.7 MB", "4.5 MB",
)

private val unknownSizes = listOf(
    "12 KB", "4 KB", "256 KB", "1.2 MB", "8 KB", "512 KB", "32 KB", "128 KB",
)

private val dates = listOf(
    "2023-10-24", "2023-10-23", "2023-10-22", "2023-10-21", "2023-10-20",
    "2023-10-19", "2023-10-18", "2023-10-17", "2023-10-16", "2023-10-15",
    "10:24 AM", "10:25 AM", "2:30 PM", "3:45 PM", "9:15 AM",
    "Yesterday", "2 days ago", "Last week", "Oct 12", "Oct 8",
)

fun getMockFilesForPath(pathName: String): List<FileItem> {
    val folders = folderNames.map { name ->
        val itemCount = (3..50).random()
        FileItem(name = name, type = FileType.Folder, date = dates.random(), size = "$itemCount items")
    }

    val images = imageNames.shuffled().take((8..15).random()).map { name ->
        FileItem(name = name, type = FileType.Image, date = dates.random(), size = imageSizes.random())
    }

    val videos = videoNames.shuffled().take((5..10).random()).map { name ->
        FileItem(name = name, type = FileType.Video, date = dates.random(), size = videoSizes.random())
    }

    val documents = documentNames.shuffled().take((8..15).random()).map { name ->
        FileItem(name = name, type = FileType.Document, date = dates.random(), size = documentSizes.random())
    }

    val unknowns = unknownNames.shuffled().take((2..5).random()).map { name ->
        FileItem(name = name, type = FileType.Unknown, date = dates.random(), size = unknownSizes.random())
    }

    return (folders + images + videos + documents + unknowns).shuffled()
}

@Composable
fun FilesRoute() {
    var pathStack by remember { mutableStateOf(listOf<String>()) }
    var isGridView by remember { mutableStateOf(false) }

    val myDrive = stringResource(R.string.my_drive)
    val currentDirName = pathStack.lastOrNull() ?: myDrive

    val currentFiles = remember(pathStack) { getMockFilesForPath(currentDirName) }

    FilesScreen(
        title = currentDirName,
        files = currentFiles,
        isGridView = isGridView,
        canGoBack = pathStack.isNotEmpty(),
        onToggleView = { isGridView = !isGridView },
        onNavigateBack = {
            if (pathStack.isNotEmpty()) pathStack = pathStack.dropLast(1)
        },
        onFileClick = { file ->
            if (file.type == FileType.Folder) {
                pathStack = pathStack + file.name
            } else {
                // TODO: 处理普通文件点击（如预览）
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesScreen(
    title: String,
    files: List<FileItem>,
    isGridView: Boolean,
    canGoBack: Boolean,
    onToggleView: () -> Unit,
    onNavigateBack: () -> Unit,
    onFileClick: (FileItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onToggleView) {
                        Icon(
                            imageVector = if (isGridView) Icons.AutoMirrored.Rounded.ViewList else Icons.Rounded.GridView,
                            contentDescription = "Toggle View"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = isGridView, label = "ViewMode") { gridMode ->
                if (gridMode) {
                    FileGridView(files = files, onFileClick = onFileClick)
                } else {
                    FileListView(files = files, onFileClick = onFileClick)
                }
            }
        }
    }
}

@Composable
fun FileListView(files: List<FileItem>, onFileClick: (FileItem) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files, key = { it.id }) { file ->
            FileListItem(file = file, onClick = { onFileClick(file) })
        }
    }
}

@Composable
fun FileListItem(file: FileItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(file.type.getBackgroundColor()),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = file.type.getIcon(),
                contentDescription = null,
                tint = file.type.getIconColor(),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = file.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = " • ${file.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        IconButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FileGridView(files: List<FileItem>, onFileClick: (FileItem) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(files, key = { it.id }) { file ->
            FileGridItem(file = file, onClick = { onFileClick(file) })
        }
    }
}

@Composable
fun FileGridItem(file: FileItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(file.type.getBackgroundColor()),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = file.type.getIcon(),
                contentDescription = null,
                tint = file.type.getIconColor(),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = file.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun FileType.getIcon(): ImageVector {
    return when (this) {
        FileType.Folder -> Icons.Rounded.Folder
        FileType.Image -> Icons.Rounded.Image
        FileType.Video -> Icons.Rounded.PlayCircle
        FileType.Document -> Icons.Rounded.Description
        FileType.Unknown -> Icons.AutoMirrored.Rounded.InsertDriveFile
    }
}

@Composable
fun FileType.getBackgroundColor(): Color {
    val isLight = MaterialTheme.colorScheme.background != Color.Black
    return when (this) {
        FileType.Folder -> if (isLight) Color(0xFFE8F0FE) else Color(0xFF1E2A3A)
        FileType.Image -> if (isLight) Color(0xFFFCE8E6) else Color(0xFF3A201E)
        FileType.Video -> if (isLight) Color(0xFFE6F4EA) else Color(0xFF1E3A26)
        FileType.Document -> if (isLight) Color(0xFFFEF7E0) else Color(0xFF3A321E)
        FileType.Unknown -> MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
fun FileType.getIconColor(): Color {
    val isLight = MaterialTheme.colorScheme.background != Color.Black
    return when (this) {
        FileType.Folder -> if (isLight) Color(0xFF1A73E8) else Color(0xFF8AB4F8)
        FileType.Image -> if (isLight) Color(0xFFD93025) else Color(0xFFF28B82)
        FileType.Video -> if (isLight) Color(0xFF1E8E3E) else Color(0xFF81C995)
        FileType.Document -> if (isLight) Color(0xFFF9AB00) else Color(0xFFFDE293)
        FileType.Unknown -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}
