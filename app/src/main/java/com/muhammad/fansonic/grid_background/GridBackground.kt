package com.muhammad.fansonic.grid_background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GridBackgroundScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterVertically
        )
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .gridBackground(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp),
                    lineColor = MaterialTheme.colorScheme.onPrimary
                ), contentAlignment = Alignment.Center
        ) {
        }
        Box(
            modifier = Modifier
                .size(200.dp)
                .ruleBackground(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp),
                    lineColor = MaterialTheme.colorScheme.onPrimary
                ), contentAlignment = Alignment.Center
        ) {
        }
        Box(
            modifier = Modifier
                .size(200.dp)
                .dottedBackground(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp),
                    dotColor = MaterialTheme.colorScheme.onPrimary
                ), contentAlignment = Alignment.Center
        ) {
        }
    }
}

@Composable
fun Modifier.ruleBackground(color: Color, lineColor: Color, spacing: Dp = 10.dp, shape: Shape) =
    background(color, shape).drawBehind {
        val spacingPx = spacing.toPx()
        val width = size.width
        val lineCountX = (width / spacingPx).toInt()
        val outline = shape.createOutline(size, layoutDirection, this)
        val path = when (outline) {
            is Outline.Generic -> outline.path
            is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
            is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
        }
        clipPath(path) {
            for (i in 0..lineCountX) {
                drawLine(
                    color = lineColor,
                    start = Offset(x = 0f, y = i * spacingPx),
                    end = Offset(x = width, y = i * spacingPx),
                    strokeWidth = 1f, cap = StrokeCap.Round
                )
            }
        }
    }

@Composable
fun Modifier.gridBackground(
    color: Color,
    lineColor: Color,
    spacing: Dp = 10.dp,
    shape: Shape = CircleShape,
) = background(color, shape).drawBehind {
    val spacingPx = spacing.toPx()
    val width = size.width
    val height = size.height
    val lineCountX = (width / spacingPx).toInt()
    val lineCountY = (height / spacingPx).toInt()
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = when (outline) {
        is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
        is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
        is Outline.Generic -> outline.path
    }
    clipPath(path) {
        for (i in 0..lineCountX) {
            val x = i * spacingPx
            drawLine(
                color = lineColor,
                start = Offset(x = x, y = 0f),
                end = Offset(x = x, y = height), strokeWidth = 1f, cap = StrokeCap.Round
            )
        }
        for (j in 0..lineCountY) {
            val y = j * spacingPx
            drawLine(
                color = lineColor,
                start = Offset(x = 0f, y = y),
                end = Offset(x = width, y = y),
                strokeWidth = 1f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun Modifier.dottedBackground(
    color: Color, dotColor: Color, spacing: Dp = 10.dp, dotRadius: Dp = 1.dp, shape: Shape,
): Modifier = this
    .background(color, shape)
    .drawBehind {
        val spacingPx = spacing.toPx()
        val dotRadiusPx = dotRadius.toPx()
        val width = size.width
        val height = size.height
        val columns = (width / spacingPx).toInt()
        val rows = (height / spacingPx).toInt()
        val outline = shape.createOutline(size, layoutDirection, this)
        val path = when (outline) {
            is Outline.Generic -> outline.path
            is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
            is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
        }
        clipPath(path) {
            for (x in 0..columns) {
                for (y in 0..rows) {
                    val offsetX = x * spacingPx
                    val offsetY = y * spacingPx
                    drawCircle(
                        color = dotColor,
                        radius = dotRadiusPx,
                        center = Offset(x = offsetX, y = offsetY)
                    )
                }
            }
        }
    }