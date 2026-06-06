package com.muhammad.fansonic.charts

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlin.math.round

@Immutable
data class LineChartData(
    val label: String,
    val value: Int,
)

@Composable
fun LineChartScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LineChart(
            chartData = listOf(
                LineChartData("Mon", 67),
                LineChartData("Tue", 50),
                LineChartData("Wed", 80),
                LineChartData("Thr", 94),
                LineChartData("Fri", 34),
                LineChartData("Sun", 20),
            ), modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    chartData: List<LineChartData>,
    lineWidth: Dp = 4.dp,
    spacingFromLeft: Float = 80f,
    spacingFromBottom: Float = 50f,
    lineColor: Color = MaterialTheme.colorScheme.background,
    labelSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    labelColor: Color = MaterialTheme.colorScheme.surface,
    color: Color = MaterialTheme.colorScheme.primary,
    transparentColor: Color = color.copy(alpha = 0.5f),
) {
    val maxValue = remember(chartData) {
        chartData.maxOfOrNull { it.value }?.plus(1) ?: 0
    }
    val minValue = remember(chartData) {
        chartData.minOfOrNull { it.value } ?: 0
    }
    Canvas(modifier = modifier) {
        val textPaint = Paint().apply {
            this.color = labelColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = labelSize.toPx()
        }
        val spacePerData = (size.width - spacingFromLeft) / chartData.size
        val verticalLabelStep = (maxValue - minValue) / 5f
        drawLine(
            color = color,
            start = Offset(x = spacingFromLeft, size.height - spacingFromBottom),
            end = Offset(x = spacingFromLeft, y = 0f),
            strokeWidth = lineWidth.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = color,
            start = Offset(x = spacingFromLeft, y = size.height - spacingFromBottom),
            end = Offset(x = size.width, y = size.height - spacingFromBottom),
            cap = StrokeCap.Round,
            strokeWidth = lineWidth.toPx()
        )
        drawContext.canvas.nativeCanvas.apply {
            for (i in chartData.indices) {
                drawText(
                    chartData[i].label,
                    spacingFromLeft + i * spacePerData,
                    size.height,
                    textPaint
                )
            }
            for (i in 0..4) {
                drawText(
                    (minValue + verticalLabelStep * i).toString(),
                    30f,
                    size.height - spacingFromLeft - i * size.height / 5f,
                    textPaint
                )
            }
        }
        val straightLinePath = Path().apply {
            val height = size.height
            chartData.indices.forEach { i ->
                val data = chartData[i]
                val x1 = spacingFromLeft + i * spacePerData
                val y1 = (maxValue - data.value).toFloat() / maxValue * height - spacingFromLeft
                if (i == 0) {
                    moveTo(x1, y1)
                }
                lineTo(x1, y1)
            }
        }
        drawPath(
            path = straightLinePath,
            color = color,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}