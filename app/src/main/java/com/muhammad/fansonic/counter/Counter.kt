package com.muhammad.fansonic.counter

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CounterScreen() {
    var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        count = 1000
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        AnimatedCounter(count = count)
    }
}

@Composable
fun AnimatedCounter(count: Int, modifier: Modifier = Modifier) {
    val animatedCounter by animateIntAsState(
        targetValue = count,
        animationSpec = tween(durationMillis = count * 1000, easing = LinearEasing)
    )
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(text = animatedCounter.toString(), style = MaterialTheme.typography.titleLarge)
    }
}