package com.muhammad.fansonic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.animation.BounceInterpolator
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt

val BouncyInterpolator = Easing {
    BounceInterpolator().getInterpolation(it)
}

@Composable
fun RiceRollerScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFECECEC), Color(0xFFDDDDDD)),
                    startY = 0f, endY = Float.POSITIVE_INFINITY
                )
            ), contentAlignment = Alignment.Center
    ) {
        DiceRoller()
    }
}

@Composable
fun DiceRoller(modifier: Modifier = Modifier, size: Dp = 160.dp) {
    var currentFace by remember { mutableIntStateOf(1) }
    var finalFace by remember { mutableIntStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    fun rollDice() {
        if (isRolling) return
        isRolling = true
        finalFace = (1..6).random()
        val duration = 1000
        scope.launch {
            val rot = launch {
                rotation.animateTo(720f, animationSpec = tween(duration, easing = LinearEasing))
            }
            launch {
                scale.animateTo(0.85f, tween(150, easing = FastOutLinearInEasing))
                scale.animateTo(1.05f, tween(250, easing = FastOutSlowInEasing))
                scale.animateTo(1f, tween(300, easing = BouncyInterpolator))
            }
            delay(500L)
            currentFace = finalFace
            rot.join()
            rotation.snapTo(0f)
            isRolling = false
        }
    }
    OnSnake(onSnake = ::rollDice)
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 12.dp)
                .blur(10.dp)
                .background(Color(0x22000000), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    rotationZ = rotation.value
                    scaleX = scale.value
                    scaleY = scale.value
                    cameraDistance = 16 * density
                }
                .shadow(10.dp, RoundedCornerShape(16.dp))
                .background(
                    Color(0xFFDA0037),
                    RoundedCornerShape(16.dp)
                )
                .clickable(enabled = !isRolling) {
                    rollDice()
                }, contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(getDiceImage(currentFace)),
                contentDescription = "Dice face $currentFace",
                modifier = Modifier.fillMaxSize(0.85f)
            )
        }
    }
}

@Composable
fun OnSnake(shakeThreshold: Float = 12f, onSnake: () -> Unit) {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val sensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    var lastSnakeTime by remember { mutableLongStateOf(0L) }
    DisposableEffect(sensorManager) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
                if (acceleration > shakeThreshold) {
                    val now = System.currentTimeMillis()
                    if (now - lastSnakeTime > 1000) {
                        lastSnakeTime = now
                        onSnake()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
}

@DrawableRes
fun getDiceImage(number: Int): Int = when (number) {
    1 -> R.drawable.dice_one
    2 -> R.drawable.dice_two
    3 -> R.drawable.dice_three
    4 -> R.drawable.dice_four
    5 -> R.drawable.dice_five
    6 -> R.drawable.dice_six
    else -> R.drawable.dice_one
}