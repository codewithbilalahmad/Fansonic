package com.muhammad.fansonic.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DrawingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        DrawingCanvas(
            modifier = Modifier.size(300.dp)
        )
    }
}

@Composable
private fun DrawingCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(0f, size.height /2)
            cubicTo(
                size.width /2f, 0f,
                size.width * 0.8f,  0f,
                size.width, size.height /2
            )
        }
        drawPath(path = path, color = Color.Cyan, style = Stroke(width = 6f, cap = StrokeCap.Round))
    }
}