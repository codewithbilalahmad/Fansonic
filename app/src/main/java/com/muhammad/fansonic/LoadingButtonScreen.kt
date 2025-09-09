package com.muhammad.fansonic

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun LoadingButtonScreen() {
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(isLoading) {
        if(isLoading){
            delay(3000)
            isLoading = false
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        LoadingButton(text = "Submit", isLoading = isLoading, onClick = {
            isLoading = true
        })
    }
}

@Composable
fun LoadingButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    width: Dp = 260.dp,
    height: Dp = 64.dp,
    gradientColors: List<Color> = listOf(
        Color(0xFFB621FE),
        Color(0xFF1FD1F9)
    ),
    contentColor: Color = Color.White,
) {
    val transition = updateTransition(targetState = isLoading, label = "Morph")
    val animatedWidth by transition.animateDp(
        label = "buttonWidth", transitionSpec = {
            spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
        }
    ) { loading ->
        if (loading) height else width
    }
    val scale by transition.animateFloat(label = "Scale", transitionSpec = {
        spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    }) { loading ->
        if (loading) 1.1f else 1f
    }
    val elevation by transition.animateDp(
        label = "Elevation",
        transitionSpec = { tween(300) }) { loading ->
        if (loading) 1.dp else 6.dp
    }
    val backgroundBrush = Brush.linearGradient(
        colors = gradientColors, start = Offset(0f, 0f), end = Offset.Infinite
    )
    Button(
        onClick = {
            onClick()
        },
        enabled = !isLoading,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(elevation),
        modifier = modifier
            .width(animatedWidth)
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundBrush, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(tween(300, easing = FastOutSlowInEasing)),
                exit = fadeOut(tween(150))
            ) {
                Text(text = text, color = contentColor, fontSize = 18.sp)
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(tween(200)) + scaleIn(tween(500, easing = FastOutSlowInEasing)),
                exit = fadeOut(tween(100)) + scaleOut(tween(100))
            ) {
                Box(modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape), contentAlignment = Alignment.Center){
                    CustomCircularLoading(size = 36.dp, strokeWidth = 5.dp)
                }
            }
        }
    }
}

@Composable
fun CustomCircularLoading(
    size: Dp, strokeWidth: Dp,
) {
    val transition = rememberInfiniteTransition(label = "ColorSpinLoader")
    val rotation by transition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        ), label = "Rotation"
    )
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "ColorShift"
    )
    val warnPalette = listOf(
        Color(0xFFFFF176), // Light Yellow
        Color(0xFFFFEE58), // Soft Yellow
        Color(0xFFFFEB3B), // Bright Yellow
        Color(0xFFFFD600), // Strong Amber
        Color(0xFFFFB300), // Golden Yellow
        Color(0xFFFFA000), // Orange-Yellow
        Color(0xFFFF8F00), // Vivid Orange
        Color(0xFFFF7043), // Soft Orange-Red
        Color(0xFFFF5252)
    )
    val steps = warnPalette.size
    val animatedColors = List(steps) { index ->
        val colorIndex = (phase * (steps - 1)) + index
        val baseIndex = colorIndex.toInt() % steps
        val nextIndex = (baseIndex + 1) % steps
        val t = colorIndex - baseIndex
        lerp(warnPalette[baseIndex], warnPalette[nextIndex], t)
    }
    val sweepBrush = Brush.sweepGradient(colors = animatedColors)
    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            rotate(rotation) {
                drawArc(
                    brush = sweepBrush,
                    startAngle = 0f,
                    sweepAngle = 260f + 10f * sin(phase * 2 * PI).toFloat(),
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}