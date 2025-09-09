package com.muhammad.fansonic.animations.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember

@Composable
fun animateValues(
    values: List<Float>,
    animationSpec: AnimationSpec<Float> = spring(),
): State<Float> {
    require(values.size >= 2) {
        "animateValues requires at least 2 values"
    }
    val groups = remember(values) { values.zipWithNext() }
    val state = remember { mutableFloatStateOf(values.first()) }
    LaunchedEffect(groups) {
        animate(
            initialValue = 0f,
            targetValue = groups.size.toFloat(),
            animationSpec = animationSpec
        ) { frame, _ ->
            val index = frame.toInt().coerceAtMost(groups.lastIndex)
            val fraction = frame - index
            val (start, end) = groups[index]
            state.floatValue = start + (end - start) * fraction
        }
    }
    return state
}