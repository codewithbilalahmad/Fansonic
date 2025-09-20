package com.muhammad.fansonic.foreground_service.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.foreground_service.service.ServiceHelper
import com.muhammad.fansonic.foreground_service.service.StopWatchService
import com.muhammad.fansonic.foreground_service.service.StopWatchState
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_CANCEL
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_START
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_STOP

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StopWatchScreen(stopWatchService: StopWatchService) {
    val hours by stopWatchService.hours
    val minutes by stopWatchService.minutes
    val seconds by stopWatchService.seconds
    val currentState by stopWatchService.currentState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            AnimatedContent(targetState = hours, transitionSpec = {
                slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeIn(
                    animationSpec = tween(durationMillis = 500)
                ) togetherWith slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeOut(
                    animationSpec = tween(durationMillis = 500)
                )
            }) { h ->
                Text(
                    text = h,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Text(
                text = ":",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            AnimatedContent(targetState = minutes, transitionSpec = {
                slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeIn(
                    animationSpec = tween(durationMillis = 500)
                ) togetherWith slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeOut(
                    animationSpec = tween(durationMillis = 500)
                )
            }) { m ->
                Text(
                    text = m,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
            Text(
                text = ":",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            AnimatedContent(targetState = seconds, transitionSpec = {
                slideInVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeIn(
                    animationSpec = tween(durationMillis = 500)
                ) togetherWith slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height } + fadeOut(
                    animationSpec = tween(durationMillis = 500)
                )
            }) { s ->
                Text(
                    text = s,
                    style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
        Spacer(Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            ElevatedButton(
                onClick = {
                    val action =
                        if (currentState == StopWatchState.STARTED) ACTION_SERVICE_STOP else ACTION_SERVICE_START
                    ServiceHelper.triggerForegroundService(action = action)
                },
                shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                val text =
                    if (currentState == StopWatchState.STARTED) "Stop" else if (currentState == StopWatchState.STOPPED) "Resume" else "Start"
                Text(text = text)
            }
            ElevatedButton(
                onClick = {
                    ServiceHelper.triggerForegroundService(action = ACTION_SERVICE_CANCEL)
                }, shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = "Cancel")
            }
        }
    }
}