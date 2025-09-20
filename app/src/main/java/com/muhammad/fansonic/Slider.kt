package com.muhammad.fansonic

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveSliderDemo() {
    var sliderValue by remember { mutableStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Volume",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..1f,
            steps = 0,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp), // 👈 taller for expressive
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                activeTickColor = MaterialTheme.colorScheme.onPrimary,
                inactiveTickColor = MaterialTheme.colorScheme.outlineVariant
            ),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    thumbSize = DpSize(28.dp, 28.dp) // 👈 bigger thumb for expressive
                )
            },
            track = { state ->
                SliderDefaults.Track(
                    sliderState = state,
                    modifier = Modifier.height(10.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${(sliderValue * 100).toInt()}%",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
