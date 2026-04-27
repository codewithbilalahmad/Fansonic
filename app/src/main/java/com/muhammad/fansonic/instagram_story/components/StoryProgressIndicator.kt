package com.muhammad.fansonic.instagram_story.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StoryProgressIndicator(
    modifier: Modifier = Modifier,
    totalSteps: Int,
    currentStep: Int,
    thickness: Dp = 2.dp,
    durationPerStep: Int = 3000,
    onStepFinished: (Int) -> Unit,
    isActive: Boolean,
    isPaused: Boolean,
) {
    val progress = remember(currentStep) { Animatable(0f) }
    var hasCompleted by remember(currentStep) { mutableStateOf(false) }
    LaunchedEffect( currentStep, isActive) {
        if (!isActive) return@LaunchedEffect
        hasCompleted = false

        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationPerStep, easing = LinearEasing)
        )

        if (!hasCompleted) {
            hasCompleted = true
            onStepFinished(currentStep)
        }
    }
    LaunchedEffect(isPaused) {
        if(isPaused){
            progress.stop()
        } else{
            if(isActive && progress.value < 1f){
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = ((1f - progress.value) * durationPerStep).toInt(),
                        easing = LinearEasing
                    )
                )
                if(!hasCompleted){
                    hasCompleted = true
                    onStepFinished(currentStep)
                }
            }
        }
    }
    Row(
        modifier = modifier.height(thickness),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val segmentProgress = when {
                index < currentStep -> 1f
                index == currentStep -> progress.value
                else -> 0f
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(segmentProgress)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.onBackground, CircleShape)
                )
            }
        }
    }
}