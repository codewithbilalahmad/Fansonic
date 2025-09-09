package com.muhammad.fansonic.animations.components.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun StepFlipAnimation(modifier: Modifier = Modifier, durationMillis: Int = 500) {
    var currentNumber by remember { mutableIntStateOf(0) }
    var frontNumber by remember { mutableIntStateOf(currentNumber) }
    var backNumber by remember { mutableIntStateOf(currentNumber) }
    var targetAngle by remember { mutableFloatStateOf(0f) }
    val rotation by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing)
    )

    fun isFront(): Boolean {
        val value = abs(rotation % 360f)
        return value < 90f || value > 270f
    }

    fun flipBack() {
        currentNumber--
        if (isFront()) {
            backNumber = currentNumber
        } else {
            frontNumber = currentNumber
        }
        targetAngle -= 180f
    }

    fun flipNext() {
        currentNumber++
        if (isFront()) {
            backNumber = currentNumber
        } else {
            frontNumber = currentNumber
        }
        targetAngle += 180f
    }
    @Composable
    fun Step(
        modifier: Modifier = Modifier,
        number: Int,
        rotationY: Float,
        containerColor: Color = MaterialTheme.colorScheme.onBackground,
        textStyle: TextStyle = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor, CircleShape)
                .graphicsLayer {
                    this.rotationY = rotationY
                }.pointerInput(Unit){
                    detectTapGestures{offset ->
                        val halfWidth = size.width /2
                        if(offset.x < halfWidth) flipBack() else flipNext()
                    }
                }) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = number.toString(),
                    modifier = Modifier.align(Alignment.Center), style = textStyle
                )
            }
        }
    }
    Box(modifier = modifier.graphicsLayer{
        rotationY = rotation
    }){
        if(isFront()){
            Step(number = frontNumber, rotationY = 0f)
        } else{
            Step(number = backNumber, rotationY = rotation)
        }
    }
}

@Preview
@Composable
private fun StepFlipAnimationPreview() {
    StepFlipAnimation(modifier = Modifier.size(width = 150.dp, height = 80.dp))
}