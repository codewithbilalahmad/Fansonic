package com.muhammad.fansonic.scroller

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun CustomVerticalZoomScrollerScreen() {
    val currentZoom  = remember { mutableFloatStateOf(1f) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CustomVerticalZoomScroller(
            currentZoom = currentZoom,
            minZoom = 1f,
            maxZoom = 100f,
            step = 0.1f, // 0.1x steps
            width = 110.dp,
            height = 170.dp
        )
    }
}

@Composable
fun CustomVerticalZoomScroller(
    currentZoom: MutableState<Float>, // State to share and update
    minZoom: Float = 1.0f,            // Minimum zoom (1x)
    maxZoom: Float = 10.0f,           // Maximum zoom (your device max)
    step: Float = 0.1f,               // Increment step (0.1x)
    width: Dp = 100.dp,               // Scroller width
    height: Dp = 160.dp               // Scroller height
) {
    // Defines how much vertical dragging is needed to change by 'step'.
    // A smaller value makes the scroller more sensitive.
    val sensitivityFactor = 30f

    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .pointerInput(Unit) {
                // This block handles the user's drag gestures.
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume() // Consume the gesture event

                    // Calculate how many steps the drag should represent
                    val stepsChanged = (-dragAmount / sensitivityFactor).roundToInt()

                    if (stepsChanged != 0) {
                        // Calculate the new zoom value
                        var newZoom = currentZoom.value + (stepsChanged * step)

                        // **Clamp the Value:** Keep zoom within [minZoom, maxZoom]
                        newZoom = newZoom.coerceIn(minZoom, maxZoom)

                        // **Step Value:** Round the result to one decimal place to ensure precise 0.1x increments
                        // This prevents floating-point precision issues like 1.0000001
                        newZoom = (newZoom * 10).roundToInt() / 10.0f

                        // Update the shared state
                        currentZoom.value = newZoom
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- The Ribbed Scroller (Visually imitating the image) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Fills the available space in the Column
                .background(Color(0xFFE0E0E0)) // Light grey background like in the image
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val scrollerWidth = size.width
                val scrollerHeight = size.height
                val numRibs = 15 // Number of vertical lines to draw
                val ribColor = Color.DarkGray
                val ribThickness = 2.dp.toPx()

                // Calculate width and center for the ribs to center the pattern
                val totalRibsWidth = numRibs * (ribThickness + 4.dp.toPx()) // (thickness + spacing)
                val startX = (scrollerWidth - totalRibsWidth) / 2f

                // Draw each vertical line
                for (i in 0 until numRibs) {
                    val lineX = startX + (i * (ribThickness + 4.dp.toPx()))

                    // To add depth, make lines slightly shorter further from the center
                    val distanceFromCenterPercent = ((numRibs / 2f - i).absoluteValue / (numRibs / 2f))
                    val heightReduction = scrollerHeight * 0.2f * distanceFromCenterPercent
                    val currentLineHeight = scrollerHeight - heightReduction

                    val lineYStart = (scrollerHeight - currentLineHeight) / 2f
                    val lineYEnd = lineYStart + currentLineHeight

                    drawLine(
                        color = ribColor,
                        start = Offset(lineX, lineYStart),
                        end = Offset(lineX, lineYEnd),
                        strokeWidth = ribThickness
                    )
                }
            }
        }
    }
}