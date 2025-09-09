package com.muhammad.fansonic

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun CircularListLayoutScreen() {
    val itemCount = 20
    CircularListLayout(itemCount = itemCount, radius = 300.dp) { index ->
        val images = listOf(
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4,
            R.drawable.pic5,
        )
        Box(contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier.size(150.dp), shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Image(
                    painter = painterResource(images[index]),
                    contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CircularListLayout(
    itemCount: Int,
    radius: Dp = 300.dp,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val radiusPx = with(density) { radius.toPx() }
    val rotation = remember { Animatable(0f) }
    val velocityTracker = remember { VelocityTracker() }
    val decay: DecayAnimationSpec<Float> = rememberSplineBasedDecay()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(onDragStart = {
                    scope.launch {
                        scope.launch { rotation.stop() }
                        velocityTracker.resetTracking()
                    }
                }, onDragEnd = {
                    val velocity = velocityTracker.calculateVelocity().x
                    scope.launch {
                        rotation.animateDecay(
                            initialVelocity = velocity * 0.2f, animationSpec = decay
                        )
                    }
                }, onDrag = { change, dragAmount ->
                    velocityTracker.addPosition(change.uptimeMillis, change.position)
                    scope.launch {
                        rotation.snapTo((rotation.value + dragAmount.x * 0.15f) % 360f)
                    }
                    change.consume()
                })
            }) {
        Layout(content = {
            repeat(itemCount) { index ->
                itemContent(index)
            }
        }, modifier = Modifier.fillMaxSize()) { measurables, constraints ->
            val layoutWidth = constraints.maxWidth
            val layoutHeight = constraints.maxHeight
            val centerX = layoutWidth.toFloat()
            val centerY = layoutHeight.toFloat()
            val angleStep = 360f / itemCount
            val placeables = measurables.map { it.measure(constraints) }
            layout(layoutWidth, layoutHeight) {
                placeables.forEachIndexed { index, placeable ->
                    val angle = index * angleStep + rotation.value - 90
                    val angleRad = Math.toRadians(angle.toDouble())
                    val x = centerX + radiusPx * cos(angleRad) - placeable.width / 2
                    val y = centerY + radiusPx * sin(angleRad) - placeable.height / 2
                    placeable.place(x.roundToInt(), y.roundToInt())
                }
            }
        }
    }
}