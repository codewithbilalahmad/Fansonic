package com.muhammad.fansonic

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val DiagonalOffset = 500f

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DiagonalPagerScreen() {
    val natureList = remember {
        mutableListOf(
            Nature(
                image = R.drawable.nature_1,
                title = "Discover Nature",
                label = "Explore the beauty of forests, mountains."
            ),
            Nature(
                image = R.drawable.nature_2,
                title = "Stay Connected",
                label = "Learn about the environment and connect."
            ),
            Nature(
                image = R.drawable.nature_3,
                title = "Daily Inspiration",
                label = "Receive calming quotes and images inspired by nature every day."
            ),
            Nature(
                image = R.drawable.nature_4,
                title = "Eco-Friendly Tips",
                label = "Simple actions you can take to live more sustainably."
            ),
            Nature(
                image = R.drawable.nature_5,
                title = "Your Nature Journey",
                label = "Start your path to mindfulness, wellness, and harmony with the earth."
            ),
        )
    }
    val pagerState = rememberPagerState { natureList.size }
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val widthPx = with(density) { screenWidth.toPx() }
    val heightPx = with(density) { screenHeight.toPx() }
    val currentPage = pagerState.currentPage
    val progress = pagerState.currentPageOffsetFraction.coerceIn(-1f, 1f)
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            BackgroundGradient(currentPage = currentPage, progress = progress)
            DiagonalPagerImages(
                state = pagerState,
                natureList = natureList,
                width = widthPx,
                height = heightPx
            )
            PageOverlay(currentPage = currentPage, natureList = natureList)
        }
    }
}

@Composable
private fun BackgroundGradient(currentPage: Int, progress: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val (startTop, startBottom) = gradientForPage(currentPage)
        val (endTop, endBottom) = gradientForPage(currentPage + 1)
        val top = lerp(startTop, endTop, progress)
        val mid = lerp(startTop, endBottom, progress)
        val bottom = lerp(startBottom, endBottom, progress)
        drawRect(brush = Brush.verticalGradient(listOf(top, mid, bottom)), size = size)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.05f), Color.Transparent),
                center = Offset(size.width / 2f, size.height * 0.8f),
                radius = size.minDimension * 0.8f
            ), size = size
        )
    }
}

@Composable
fun DiagonalPagerImages(state: PagerState, natureList: List<Nature>, width: Float, height: Float) {
    val currentPage = state.currentPage
    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize(),
        beyondViewportPageCount = 1
    ) { page ->
        val offsetX = when (page) {
            currentPage -> state.currentPageOffsetFraction * width
            currentPage - 1 -> (state.currentPageOffsetFraction + 1f) * width
            currentPage + 1 -> (state.currentPageOffsetFraction - 1f) * width
            else -> return@HorizontalPager
        }
        DiagonalImage(
            image = natureList[page].image,
            offset = Offset(x = offsetX, y = 0f),
            width = width,
            height = height
        )
    }
}

@Composable
private fun PageOverlay(currentPage: Int, natureList: List<Nature>) {
    val currentNature = natureList[currentPage]
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 80.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            PageIndicator(pageSize = natureList.size, currentPage = currentPage)
            Spacer(Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                FadingText(text = currentNature.title, fontSize = 24.sp)
                Spacer(Modifier.height(4.dp))
                FadingText(text = currentNature.label, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun DiagonalImage(image: Int, offset: Offset, width: Float, height: Float) {
    val bitmap = ImageBitmap.imageResource(image)
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        val midY = height / 1.1f
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width, midY - DiagonalOffset)
            lineTo(0f, midY)
            close()
        }
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()
        val canvasAspect = size.width / size.height
        val scaledWidth: Float
        val scaledHeight: Float
        if (aspectRatio > canvasAspect) {
            scaledHeight = size.height
            scaledWidth = scaledHeight * aspectRatio
        } else {
            scaledWidth = size.width
            scaledHeight = scaledWidth / aspectRatio
        }
        val left = (size.width - scaledWidth) / 2f
        translate(offset.x, offset.y) {
            clipPath(path) {
                drawImage(
                    image = bitmap,
                    dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt()),
                    dstOffset = IntOffset(left.toInt(), 0)
                )
            }
        }
    }
}

@Composable
fun PageIndicator(
    pageSize: Int,
    currentPage: Int,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.White.copy(0.3f),
    activeSize: Dp = 12.dp,
    inactiveSize: Dp = 8.dp,
    spacing: Dp = 8.dp,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        repeat(pageSize) { index ->
            val isSelected = index == currentPage
            val size by animateDpAsState(
                targetValue = if (isSelected) activeSize else inactiveSize, label = "dotSize"
            )
            val color by animateColorAsState(
                targetValue = if (isSelected) activeColor else inactiveColor, label = "dotColor"
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .background(color, CircleShape)
            )
        }
    }
}

@Composable
fun FadingText(text: String, fontSize: TextUnit) {
    AnimatedContent(
        targetState = text,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "fadingText"
    ) { label ->
        Text(text = label, style = TextStyle(fontSize = fontSize, color = Color.White))
    }
}

private fun gradientForPage(page: Int): Pair<Color, Color> = when (page % 3) {
    0 -> Color(0xFF4A4A4A) to Color(0xFF2F2F2F)
    1 -> Color(0xFF5A5A5A) to Color(0xFF3A3A3A)
    else -> Color(0xFF3C3C3C) to Color(0xFF252525)
}

data class Nature(
    val image: Int, val title: String, val label: String,
)

