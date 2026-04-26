package com.muhammad.fansonic.movie_app

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun DraggableCard(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    item: Any,
    enabled: Boolean,
    onSwiped: (SwipeResult, Any) -> Unit, content: @Composable () -> Unit,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat()
    val swipeLimit = screenWidth * 3f
    val swipeX = remember { Animatable(0f) }
    val swipeY = remember { Animatable(0f) }
    val rotation = (swipeX.value / screenWidth * 15f).coerceIn(-30f, 30f)
    Card(
        modifier = modifier
            .then(
                if (enabled) Modifier.dragContent(
                    swipeX = swipeX,
                    swipeY = swipeY,
                    max = swipeLimit,
                    onSwiped = onSwiped,
                    item = item
                ) else Modifier
            )
            .graphicsLayer {
                translationX = swipeX.value
                translationY = swipeY.value
                rotationZ = rotation
            },
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        content()
    }
}

@Composable
private fun Modifier.dragContent(
    item: Any,
    swipeX: Animatable<Float, AnimationVector1D>,
    swipeY: Animatable<Float, AnimationVector1D>,
    max: Float,
    onSwiped: (SwipeResult, Any) -> Unit,
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    pointerInput(Unit) {
        detectDragGestures(onDragCancel = {
            coroutineScope.launch {
                swipeX.animateTo(0f, tween(300))
                swipeY.animateTo(0f, tween(300))
            }
        }, onDragEnd = {
            coroutineScope.launch {
                val threshold = max * 0.3f
                if (abs(swipeX.value) < threshold) {
                    swipeX.animateTo(0f, tween(300))
                    swipeY.animateTo(0f, tween(300))
                } else {
                    val target = if (swipeX.value > 0) max else -max
                    swipeX.animateTo(target, tween(300))
                    onSwiped(if (swipeX.value > 0) SwipeResult.ACCEPTED else SwipeResult.REJECTED, item)
                }
            }
        }, onDrag = { change, dragAmount ->
            change.consume()
            coroutineScope.launch {
                swipeX.snapTo(swipeX.value + dragAmount.x)
                swipeY.snapTo(swipeY.value + dragAmount.y)
            }
        })
    }
}