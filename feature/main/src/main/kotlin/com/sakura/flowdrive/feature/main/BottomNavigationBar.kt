package com.sakura.flowdrive.feature.main

import android.os.Build
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.github.fletchmckee.liquid.LiquidState
import io.github.fletchmckee.liquid.liquid
import io.github.fletchmckee.liquid.liquefiable
import io.github.fletchmckee.liquid.rememberLiquidState
import kotlinx.coroutines.launch

val LocalBottomNavigationLiquid = compositionLocalOf<LiquidState> {
    error("LiquidState not provided")
}

fun isLiquidFrostAvailable(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

@Composable
fun BottomNavigationBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (Int) -> Unit,
    currentPageIndex: Int,
    modifier: Modifier = Modifier,
) {
    val liquidState = rememberLiquidState()
    val useLiquid = isLiquidFrostAvailable()

    CompositionLocalProvider(
        LocalBottomNavigationLiquid provides liquidState
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            MainBottomBar(
                tabs = destinations,
                currentPageIndex = currentPageIndex,
                onNavigateToDestination = onNavigateToDestination,
                useLiquid = useLiquid,
            )
        }
    }
}

@Composable
private fun MainBottomBar(
    tabs: List<TopLevelDestination>,
    currentPageIndex: Int,
    onNavigateToDestination: (Int) -> Unit,
    useLiquid: Boolean,
    modifier: Modifier = Modifier,
) {
    val liquidState = LocalBottomNavigationLiquid.current
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    val itemPositions = remember { mutableMapOf<Int, Pair<Float, Float>>() }
    var selectedItemPos by remember { mutableStateOf<Pair<Float, Float>?>(null) }

    val rowHorizontalPaddingDp = 6.dp
    val rowHorizontalPaddingPx = with(density) { rowHorizontalPaddingDp.toPx() }
    val indicatorHorizontalInsetPx = with(density) { 4.dp.toPx() }

    val indicatorX = remember { androidx.compose.animation.core.Animatable(0f) }
    val indicatorWidth = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(currentPageIndex, selectedItemPos) {
        val raw = selectedItemPos ?: itemPositions[currentPageIndex] ?: return@LaunchedEffect
        val targetX = raw.first + rowHorizontalPaddingPx - indicatorHorizontalInsetPx
        val targetW = raw.second + indicatorHorizontalInsetPx * 2f
        launch {
            indicatorX.animateTo(
                targetValue = targetX,
                animationSpec = spring(
                    dampingRatio = 0.7f,
                    stiffness = Spring.StiffnessMediumLow
                ),
            )
        }
        launch {
            indicatorWidth.animateTo(
                targetValue = targetW,
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = Spring.StiffnessMedium
                ),
            )
        }
    }

    val isDarkTheme = !MaterialTheme.colorScheme.background.luminance().let { it > 0.5f }

    val glassBackgroundBrush = Brush.linearGradient(
        colors = if (isDarkTheme) {
            listOf(
                Color(0xFF2A2A2A).copy(alpha = 0.65f),
                Color(0xFF1A1A1A).copy(alpha = 0.45f)
            )
        } else {
            listOf(
                Color.White.copy(alpha = 0.85f),
                Color.White.copy(alpha = 0.60f)
            )
        }
    )

    val glassBorderBrush = Brush.linearGradient(
        colors = if (isDarkTheme) {
            listOf(
                Color.White.copy(alpha = 0.35f),
                Color.White.copy(alpha = 0.05f),
                Color.White.copy(alpha = 0.15f)
            )
        } else {
            listOf(
                Color.White.copy(alpha = 0.9f),
                Color.White.copy(alpha = 0.3f),
                Color.White.copy(alpha = 0.6f)
            )
        }
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                shadowElevation = 24.dp.toPx()
                shape = CircleShape
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(glassBackgroundBrush)
                .border(1.dp, glassBorderBrush, CircleShape)
                .then(
                    if (useLiquid) {
                        Modifier.liquid(liquidState) {
                            this.shape = CircleShape
                            this.frost = if (isDarkTheme) 16.dp else 12.dp
                            this.curve = if (isDarkTheme) .45f else .55f
                            this.refraction = if (isDarkTheme) .12f else .15f
                            this.dispersion = if (isDarkTheme) .20f else .25f
                            this.saturation = if (isDarkTheme) 1.2f else 1.1f
                            this.contrast = if (isDarkTheme) 1.2f else 1.0f
                        }
                    } else {
                        Modifier
                    }
                )
                .then(if (useLiquid) Modifier.liquefiable(liquidState) else Modifier),
        ) {
            val indicatorWidthValue = indicatorWidth.value
            val indicatorXValue = indicatorX.value

            if (indicatorWidthValue > 0f) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(vertical = 4.dp)
                        .zIndex(0f)
                ) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer { translationX = indicatorXValue }
                            .width(with(density) { indicatorWidthValue.toDp() })
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        if (isDarkTheme) Color.White.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.45f),
                                        Color.Transparent
                                    ),
                                    radius = with(density) { (indicatorWidthValue * 0.8f) }
                                )
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        if (isDarkTheme) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.6f),
                                        Color.Transparent,
                                        if (isDarkTheme) Color.White.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(1.dp)
                                .align(Alignment.TopCenter)
                                .padding(top = 1.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            if (isDarkTheme) Color.White.copy(alpha = 0.4f) else Color.White.copy(alpha = 0.8f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = rowHorizontalPaddingDp, vertical = 4.dp)
                    .zIndex(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                tabs.forEachIndexed { index, destination ->
                    LiquidGlassTabItem(
                        destination = destination,
                        isSelected = index == currentPageIndex,
                        isDarkTheme = isDarkTheme,
                        onSelect = { onNavigateToDestination(index) },
                        onPositioned = { x, width ->
                            if (itemPositions[index]?.first != x || itemPositions[index]?.second != width) {
                                itemPositions[index] = x to width
                                if (index == currentPageIndex) {
                                    selectedItemPos = x to width
                                }
                            }
                            if (index == currentPageIndex && indicatorWidth.value == 0f) {
                                val snapX = x + rowHorizontalPaddingPx - indicatorHorizontalInsetPx
                                val snapW = width + indicatorHorizontalInsetPx * 2f
                                scope.launch { indicatorX.snapTo(snapX) }
                                scope.launch { indicatorWidth.snapTo(snapW) }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun LiquidGlassTabItem(
    destination: TopLevelDestination,
    isSelected: Boolean,
    isDarkTheme: Boolean,
    onSelect: () -> Unit,
    onPositioned: (x: Float, width: Float) -> Unit,
) {
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "pressScale",
    )

    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "iconScale",
    )

    val iconOffsetY by animateDpAsState(
        targetValue = if (isSelected) (-2).dp else 1.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "iconOffsetY",
    )

    val activeColor = if (isDarkTheme) Color.White else MaterialTheme.colorScheme.primary
    val inactiveColor = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)

    val iconTint = if (isSelected) activeColor else inactiveColor

    val labelAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = if (isSelected) 250 else 150, easing = FastOutSlowInEasing),
        label = "labelAlpha",
    )

    val labelScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "labelScale",
    )

    val horizontalPadding by animateDpAsState(
        targetValue = if (isSelected) 16.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "hPadding",
    )

    val icon = if (isSelected) destination.filledIcon else destination.outlinedIcon

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) { onSelect() }
            .onGloballyPositioned { coordinates ->
                val x = coordinates.positionInParent().x
                val width = coordinates.size.width.toFloat()
                onPositioned(x, width)
            }
            .graphicsLayer {
                scaleX = pressScale
                scaleY = pressScale
            }
            .padding(horizontal = horizontalPadding, vertical = 6.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = destination.label,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                        translationY = with(density) { iconOffsetY.toPx() }
                    },
                tint = iconTint,
            )

            Box(
                modifier = Modifier
                    .height(if (isSelected) 14.dp else 0.dp)
                    .graphicsLayer {
                        alpha = labelAlpha
                        scaleX = labelScale
                        scaleY = labelScale
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = destination.label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        lineHeight = 12.sp,
                    ),
                    color = if (isSelected) activeColor else inactiveColor,
                    maxLines = 1,
                )
            }
        }
    }
}
