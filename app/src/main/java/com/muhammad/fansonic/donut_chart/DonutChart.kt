package com.muhammad.fansonic.donut_chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.core.content.res.ResourcesCompat
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    calories: Int,
    nutrition: List<Nutrition>
) {
    val context = LocalContext.current
    val containerColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onBackground
    val latoTypeface = ResourcesCompat.getFont(context, R.font.lato)
    val total = nutrition.sumOf { it.percentage.toDouble()}.toFloat()
    Box(modifier = modifier, contentAlignment = Alignment.Center){
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 45f
            val radius = size.minDimension / 2.2f
            val arcRect = Rect(
                offset = Offset(
                    x = (size.width - radius * 2) / 2,
                    y = (size.height - radius * 2) / 2
                ), size = Size(width = radius * 2, height = radius * 2)
            )
            var startAngle = -90f
            nutrition.forEach { nut->
                val sweepAngle = (nut.percentage / total) * 360f
                drawArc(
                    color = nut.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth),
                    topLeft = arcRect.topLeft,
                    size = arcRect.size
                )
                val angleInRad = Math.toRadians((startAngle + sweepAngle / 2).toDouble())
                val labelRadius = radius + 50f
                val x = center.x + (labelRadius * cos(angleInRad)).toFloat()
                val y = center.y + (labelRadius * sin(angleInRad)).toFloat()
                val text = "${nut.percentage}%"
                val textPaint = Paint().apply {
                    textSize = 35f
                    typeface = latoTypeface
                    textAlign = Paint.Align.CENTER
                    isFakeBoldText = true
                    color = containerColor.toArgb()
                    isAntiAlias = true
                }
                val textWidth = textPaint.measureText(text)
                val textHeight = textPaint.descent() - textPaint.ascent()
                val paddingX = 28f
                val paddingY = 22f
                val rectLeft = x - textWidth / 2 - paddingX
                val rectTop = y - textHeight / 2 - paddingY
                val rectRight = x + textWidth / 2 + paddingX
                val rectBottom = y + textHeight / 2 + paddingY
                drawRoundRect(
                    color = contentColor,
                    topLeft = Offset(x = rectLeft, y = rectTop),
                    size = Size(width = rectRight - rectLeft, height = rectBottom - rectTop),
                    cornerRadius = CornerRadius(60f, 60f)
                )
                drawContext.canvas.nativeCanvas.drawText(
                    text,  x , y - (textPaint.ascent() + textPaint.descent()) /2,textPaint
                )
                startAngle += sweepAngle
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$calories",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = stringResource(R.string.kcal),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}