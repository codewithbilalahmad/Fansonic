package com.muhammad.fansonic

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import kotlin.math.max

@Composable
fun CircularImageRevealScreen() {
    val images = listOf(
        R.drawable.pic1,
        R.drawable.pic2,
        R.drawable.pic3,
        R.drawable.pic4,
        R.drawable.pic5
    )
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    var currentImage by remember { mutableIntStateOf(images.random()) }
    var nextImage by remember { mutableStateOf<Int?>(null) }
    val nextBitmap = nextImage?.let { img -> ImageBitmap.imageResource(img) }
    val isRevealing = nextImage != null
    val density = LocalDensity.current
    val fabSize = with(density) { 56.dp.toPx() }
    val fabPadding = with(density) { 16.dp.toPx() }
    val fabCenter = Offset(
        x = screenSize.width - fabSize / 2f - fabPadding,
        y = screenSize.height - fabSize / 2f - fabPadding
    )
    val progress by animateFloatAsState(
        targetValue = if (isRevealing) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "revealProgress",
        finishedListener = {
            nextImage?.let { img ->
                currentImage = img
                nextImage = null
            }
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                screenSize = coordinates.size
            }) {
        Image(
            painter = painterResource(currentImage),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        if (isRevealing && nextBitmap != null) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = progress * hypot(size.width, size.height)
                val revealPath = Path().apply {
                    addOval(Rect(center = fabCenter, radius = radius))
                }
                clipPath(revealPath) {
                    val scale = max(
                        size.width / nextBitmap.width.toFloat(),
                        size.height / nextBitmap.height.toFloat()
                    )
                    val dstSize = IntSize(
                        width = (nextBitmap.width * scale).toInt(),
                        height = (nextBitmap.height * scale).toInt()
                    )
                    val dstOffset = IntOffset(
                        x = ((size.width - dstSize.width) / 2f).toInt(),
                        y = ((size.height - dstSize.height) / 2f).toInt()
                    )
                    drawImage(
                        image = nextBitmap,
                        dstSize = dstSize,
                        dstOffset = dstOffset
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = {
                if (!isRevealing) {
                    nextImage = images.filter { img -> img != currentImage }.random()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 50.dp),
            shape = CircleShape,
            containerColor = Color(0xFFFF2D95)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_next),
                contentDescription = "Next Image", tint = Color.White
            )
        }
    }
}