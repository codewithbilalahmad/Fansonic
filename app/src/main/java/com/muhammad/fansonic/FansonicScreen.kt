package com.muhammad.fansonic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FansonicScreen() {
    var speedAnimation by remember { mutableFloatStateOf(0f) }
    var isFanOn by remember { mutableStateOf(false) }
    var fanSpeed by remember { mutableFloatStateOf(70f) }
    val animatedSpeed by animateFloatAsState(
        targetValue = fanSpeed, animationSpec = tween(durationMillis = 300, easing = LinearEasing)
    )
    LaunchedEffect(fanSpeed) {
        speedAnimation = animatedSpeed
    }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isFanOn) (speedAnimation * 3.6f) else 0f,
        animationSpec = if (isFanOn) {
            infiniteRepeatable(
                tween(
                    durationMillis = (100 - (fanSpeed / 100 * 60).toInt()),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        } else tween(durationMillis = 0)
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
                .height(360.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Speedometer(
                currentSpeed = animatedSpeed,
                modifier = Modifier
                    .padding(90.dp)
                    .requiredSize(300.dp)
            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_propeller),
                contentDescription = "propeller",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    })
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp), horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = {
                        fanSpeed -= 10f
                    }, enabled = fanSpeed > 0f,
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(topEnd = 30.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_minus),
                        contentDescription = null
                    )
                }
                Spacer(Modifier.width(10.dp))
                OutlinedButton(
                    onClick = {
                        fanSpeed += 10f
                    }, enabled = fanSpeed < 100f,
                    border = BorderStroke(1.dp, Color.Gray),
                    shape = RoundedCornerShape(topStart = 30.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(Modifier.height(50.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                label = if (isFanOn) "OFF" else "ON",
                onClick = {
                    isFanOn = !isFanOn
                },
                shape = RoundedCornerShape(topStart = 20.dp),
                icon = R.drawable.ic_power
            )
            ActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                label = "12°C",
                onClick = {
                },
                shape = RoundedCornerShape(topEnd = 20.dp),
                icon = R.drawable.ic_temperature
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                label = "125 rpm",
                onClick = {
                },
                shape = RoundedCornerShape(bottomStart = 20.dp),
                icon = R.drawable.ic_speed
            )
            ActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                label = "95%",
                onClick = {
                },
                shape = RoundedCornerShape(bottomEnd = 20.dp),
                icon = R.drawable.ic_battery
            )
        }
    }
}