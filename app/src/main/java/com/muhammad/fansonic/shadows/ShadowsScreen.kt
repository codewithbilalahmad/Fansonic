package com.muhammad.fansonic.shadows

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.fansonic.R

private val Black900 = Color(0xFF000000)
private val Black950 = Color(0xFF0A0A0A)
private val Gray400 = Color(0xFFB0B0B0)
private val Gray900 = Color(0xFF1F1F1F)
private val White900 = Color(0xFFFFFFFF)
private val Cyan100 = Color(0xFF00FFFF)
private val TechBlack = Color(0xFF0B0B0D)

@Immutable
data class ShadowItem(
    val modifier: Modifier,
    val title: String,
    val description: String,
    val borderColor: Color,
    val icon: Int = R.drawable.ic_star,
)

@Immutable
data class ShadowConfig(
    val strokeColor: Color = Color(0xFFFF9718),
    val fillColor: Color = Color(0xFFFF5722),
    val secondaryFillColor: Color = Color(0xFF536DFE),
    val glassReflectColor: Color = Color(0xFFF3DFAC),
    val themeColor: Color = Black900,
)

@Composable
fun ShadowsScreen() {
    val samples = remember { createShadowSamples() }
    var currentIndex by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TechBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val sample = samples[currentIndex]
        ShadowComposable(
            modifier = sample.modifier,
            title = sample.title,
            description = sample.description,
            borderColor = sample.borderColor,
            icon = ImageVector.vectorResource(sample.icon)
        )
        Spacer(Modifier.height(30.dp))
        NavigationControl(onPrevious = {
            currentIndex = if (currentIndex - 1 < 0) samples.lastIndex else currentIndex - 1
        }, onNext = {
            currentIndex = if (currentIndex + 1 > samples.lastIndex) 0 else currentIndex + 1
        })
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun NavigationControl(onPrevious: () -> Unit, onNext: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPrevious,
            shapes = IconButtonDefaults.shapes(),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_backward),
                contentDescription = null
            )
        }
        IconButton(
            onClick = onNext, shapes = IconButtonDefaults.shapes(),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_forward),
                contentDescription = null
            )
        }
    }
}

private fun createShadowSamples(): List<ShadowItem> {
    return listOf(
        createFillShadowSimple(),
        createGradientFillShadowSimple(),
        createCyanVolumetricShadowSample(
            ShadowConfig(
                strokeColor = Color(0xFF00F5FF),
                fillColor = Color(0xFFFF00FF),
                secondaryFillColor = Color(0xFF9D00FF),
                glassReflectColor = Color(0xFFFFFFFF)
            )
        ),
        createVolumetricShadowSimple(
            ShadowConfig(
                strokeColor = Color(0xFF00FF99),
                fillColor = Color(0xFF00FF7F),
                secondaryFillColor = Color(0xFF39FF14),
                glassReflectColor = Color(0xFFFFFFFF),
            )
        ), createInnerDropShadowSample()
    )
}

private fun createGradientFillShadowSimple(): ShadowItem {
    val config = ShadowConfig(
        strokeColor = Color(0xFFFFD500),
        fillColor = Color(0xFFFF6B35),
        secondaryFillColor = Color(0xFFFFD500)
    )
    val modifier = Modifier
        .width(200.dp)
        .wrapContentHeight()
        .dropShadow(
            shape = RoundedCornerShape(42.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(12.dp, 10.dp),
                color = config.strokeColor
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(42.dp), shadow = Shadow(
                radius = 2.dp,
                offset = DpOffset(8.dp, 6.dp),
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.25f to config.fillColor,
                        0.75f to config.secondaryFillColor,
                    ), start = Offset.Infinite.copy(y = 0f), end = Offset.Infinite.copy(x = 0f)
                )
            )
        )
        .background(config.themeColor, RoundedCornerShape(40.dp))
        .graphicsLayer {
            clip = true
        }
    return ShadowItem(
        modifier = modifier,
        title = "Gradient Fill Shadow",
        description = "Linear gradient shadow transitioning from orange to gold for dynamic effects.",
        borderColor = config.strokeColor
    )
}

private fun createFillShadowSimple(): ShadowItem {
    val config = ShadowConfig(strokeColor = Color(0xFFFA0505))
    val modifier = Modifier
        .width(200.dp)
        .wrapContentHeight()
        .dropShadow(
            shape = RoundedCornerShape(42.dp),
            shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(12.dp, 10.dp),
                color = config.strokeColor
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(38.dp),
            shadow = Shadow(
                radius = 2.dp,
                offset = DpOffset(8.dp, 6.dp),
                color = Color(0xFFFAE0E0)
            )
        )
        .background(config.themeColor, RoundedCornerShape(40.dp))
        .graphicsLayer {
            clip = true
        }
    return ShadowItem(
        modifier = modifier,
        title = "Chaining Drop Shadows",
        description = "Chaining two drop shadows with solid color fills to create depth and dimension.",
        borderColor = config.strokeColor
    )
}

private fun createVolumetricShadowSimple(
    config: ShadowConfig,
    title: String = "Volumetric Shadows",
    description: String = "Layering multiple gradient shadows to create realistic 3D depth and dimension.",
): ShadowItem {
    val modifier = Modifier
        .width(200.dp)
        .wrapContentHeight()
        .dropShadow(
            shape = RoundedCornerShape(40.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(14.dp, 10.dp),
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.05f to config.strokeColor,
                        0.40f to config.strokeColor,
                        0.70f to config.glassReflectColor.copy(alpha = 0.5f),
                        0.85f to Color.Transparent,
                        1f to config.strokeColor.copy(alpha = 0.025f)
                    )
                )
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(40.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(10.dp, 10.dp),
                brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0.40f to config.strokeColor,
                        0.70f to config.glassReflectColor.copy(alpha = 0.5f),
                        0.85f to Color.Transparent,
                        1f to config.strokeColor.copy(alpha = 0.025f)
                    )
                )
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(36.dp), shadow = Shadow(
                radius = 2.dp,
                offset = DpOffset(8.dp, 6.dp),
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.35f to config.fillColor,
                        0.50f to Color.Black,
                        0.75f to Color.Black,
                        0.90f to config.fillColor
                    ), start = Offset.Infinite.copy(y = 0f), end = Offset.Infinite.copy(x = 0f)
                )
            )
        )
        .background(config.themeColor, RoundedCornerShape(40.dp))
        .graphicsLayer {
            clip = true
        }
    return ShadowItem(
        modifier = modifier,
        title = title,
        description = description,
        borderColor = config.strokeColor
    )
}

private fun createCyanVolumetricShadowSample(
    config: ShadowConfig,
    title: String = "Glass Effect?",
    description: String = "Combining multiple gradient shadows with transparent color stops to simulate a glassy effect.",
): ShadowItem {
    val modifier = Modifier
        .width(200.dp)
        .wrapContentHeight()
        .dropShadow(
            shape = RoundedCornerShape(40.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(x = 14.dp, y = 10.dp),
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.40f to config.strokeColor,
                        0.70f to config.glassReflectColor.copy(alpha = 0.5f),
                        0.85f to Color.Transparent,
                        1f to config.strokeColor.copy(alpha = 0.025f)
                    )
                )
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(40.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(x = 10.dp, y = 10.dp),
                brush = Brush.horizontalGradient(
                    colorStops = arrayOf(
                        0.40f to config.strokeColor,
                        0.70f to config.glassReflectColor.copy(alpha = 0.5f),
                        0.85f to Color.Transparent,
                        1f to config.strokeColor.copy(alpha = 0.025f)
                    )
                )
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(36.dp), shadow = Shadow(
                radius = 2.dp,
                offset = DpOffset(x = 8.dp, y = 6.dp),
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.025f to Color.White,
                        0.35f to config.fillColor,
                        0.50f to Color.Black,
                        0.75f to Color.Black,
                        0.90f to config.fillColor,
                        1f to Color.White
                    ), start = Offset.Infinite.copy(y = 0f), end = Offset.Infinite.copy(x = 0f)
                )
            )
        )
        .background(config.themeColor, RoundedCornerShape(40.dp))
        .graphicsLayer {
            clip = true
        }
    return ShadowItem(
        modifier = modifier,
        title = title,
        description = description,
        borderColor = config.strokeColor
    )
}

private fun createInnerDropShadowSample(): ShadowItem {
    val config = ShadowConfig(
        strokeColor = Color(0xFF18C9FF),
        fillColor = Color(0xFFFF4081),
        glassReflectColor = Color(0xFF40FFE9),
        secondaryFillColor = Color(0xFF525B5B)
    )
    val modifier = Modifier
        .width(200.dp)
        .wrapContentHeight()
        .dropShadow(
            shape = RoundedCornerShape(40.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(x = 14.dp, y = 10.dp),
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.40f to config.strokeColor,
                        0.70f to config.glassReflectColor.copy(alpha = 0.5f),
                        0.85f to config.glassReflectColor.copy(alpha = 0.15f),
                        1f to config.strokeColor.copy(alpha = 0.025f)
                    )
                )
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(40.dp), shadow = Shadow(
                radius = 1.dp,
                offset = DpOffset(x = 10.dp, y = 10.dp),
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.40f to config.strokeColor,
                        0.70f to config.glassReflectColor.copy(alpha = 0.5f),
                        0.85f to config.glassReflectColor.copy(alpha = 0.15f),
                        1f to config.strokeColor.copy(alpha = 0.025f)
                    )
                )
            )
        )
        .dropShadow(
            shape = RoundedCornerShape(44.dp), shadow = Shadow(
                radius = 2.dp, offset = DpOffset(8.dp, 6.dp), brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0.05f to Color.White,
                        0.15f to config.secondaryFillColor,
                        0.35f to config.fillColor,
                        0.45f to config.secondaryFillColor,
                        0.50f to Color.Black,
                        0.75f to Color.Black,
                        0.85f to config.fillColor,
                        0.90f to config.secondaryFillColor,
                        1f to Color.White
                    ), start = Offset.Infinite.copy(y = 0f), end = Offset.Infinite.copy(x = 0f)
                )
            )
        )
        .background(config.themeColor, RoundedCornerShape(40.dp))
        .innerShadow(
            shape = RoundedCornerShape(42.dp), shadow = Shadow(
                radius = 50.dp, offset = DpOffset(10.dp, -(10).dp),
                brush = Brush.linearGradient(
                    colors = listOf(
                        White900,
                        config.themeColor,
                        Color.Red,
                        config.themeColor
                    )
                )
            )
        )
        .innerShadow(
            shape = RoundedCornerShape(42.dp), shadow = Shadow(
                radius = 50.dp, offset = DpOffset(10.dp, -(10).dp),
                brush = Brush.linearGradient(
                    colors = listOf(
                        config.themeColor,
                        Cyan100,
                        config.themeColor,
                        White900
                    )
                )
            )
        )
        .graphicsLayer {
            clip = true
        }
    return ShadowItem(
        modifier = modifier,
        title = "Inner + Drop\nShadows",
        description = "Looks like background blur? It's actually inner shadows with high radius values.",
        borderColor = config.strokeColor
    )
}

@Composable
fun ShadowComposable(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    borderColor: Color,
    icon: ImageVector,
) {
    Box(
        modifier = modifier
            .border(
                width = 4.dp,
                color = borderColor,
                shape = RoundedCornerShape(36.dp)
            )
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
            AnimatedContent(targetState = icon, transitionSpec = {
                (fadeIn() + slideInHorizontally { it / 2 }) togetherWith (fadeOut() + slideOutHorizontally { -it / 2 })
            }, label = "IconAnimated") { targetIcon ->
                IconBox(icon = targetIcon)
            }
            Spacer(Modifier.height(24.dp))
            AnimatedContent(targetState = title, transitionSpec = {
                (fadeIn() + slideInHorizontally { it / 4 }) togetherWith (fadeOut() + slideOutHorizontally { -it / 4 })
            }, label = "TitleAnimation") { targetTitle ->
                CardTitle(title = targetTitle)
            }
            Spacer(Modifier.height(12.dp))
            AnimatedContent(targetState = description, transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }) { targetDescription ->
                CardDescription(description = targetDescription)
            }
        }
    }
}

@Composable
fun IconBox(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .innerShadow(
                shape = RoundedCornerShape(12.dp), shadow = Shadow(
                    radius = 12.dp, brush = Brush.linearGradient(
                        colors = listOf(White900, Gray900), start = Offset(x = 90f, y = -90f)
                    )
                )
            )
            .border(width = 1.dp, color = Black950, shape = RoundedCornerShape(12.dp))
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CardTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold,
        lineHeight = 24.sp
    )
}

@Composable
private fun CardDescription(description: String) {
    Text(
        text = description, color = Gray400, fontSize = 14.sp
    )
}

