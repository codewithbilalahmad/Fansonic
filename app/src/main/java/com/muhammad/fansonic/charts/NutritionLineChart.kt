package com.muhammad.fansonic.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max

// ─────────────────────────────────────────
// Data model
// ─────────────────────────────────────────

@Immutable
data class NutritionChartPoint(
    val label: String,   // e.g. "Sat\n30"
    val value: Float     // 0f means "no data" → dot sits on baseline
)

// ─────────────────────────────────────────
// Preview / sample usage
// ─────────────────────────────────────────

@Composable
fun NutritionLineChartScreen() {
    Column {
        NutritionProgressChart(
            title     = "Carbs",
            lineColor = Color(0xFF73C441),
            points    = listOf(
                NutritionChartPoint("Sat\n30",  26f),
                NutritionChartPoint("Sun\n31",   0f),
                NutritionChartPoint("Mon\n1",   58f),
                NutritionChartPoint("Tue\n2",    0f),
                NutritionChartPoint("Wed\n3",    0f),
                NutritionChartPoint("Today",     0f),
                NutritionChartPoint("Fri\n5",    0f)
            )
        )
    }
}

// ─────────────────────────────────────────
// Chart
// ─────────────────────────────────────────

@Composable
fun NutritionProgressChart(
    title     : String,
    lineColor : Color,
    points    : List<NutritionChartPoint>,
    modifier  : Modifier = Modifier
) {
    if (points.size < 2) return

    val textMeasurer = rememberTextMeasurer()

    // Max for Y-axis ticks — rounded up to a nice number
    val rawMax = remember(points) {
        max(points.maxOfOrNull { it.value } ?: 10f, 10f)
    }
    // Round up so the top grid line is a clean value (e.g. 58 → 75)
    val maxValue = remember(rawMax) {
        val step = when {
            rawMax <= 20f  -> 5f
            rawMax <= 50f  -> 25f
            rawMax <= 100f -> 25f
            else           -> 50f
        }
        (kotlin.math.ceil(rawMax / step) * step).toFloat()
    }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {

        Text(
            text       = title,
            style      = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            // ── Dimensions ────────────────────────────────────────────
            val yLabelWidth  = 52f          // space for "75g" on the left
            val rightGap     = 16f
            val topGap       = 36f          // room above top grid line for peak labels
            val bottomGap    = 52f          // room for x-axis day labels

            val chartLeft    = yLabelWidth
            val chartRight   = size.width - rightGap
            val chartTop     = topGap
            // The BASELINE is slightly above the true bottom — zero-value dots sit here
            // Matches the screenshot: baseline ≈ 88% down the chart area
            val chartBottom  = size.height - bottomGap
            val chartHeight  = chartBottom - chartTop

            val n = points.size

            // ── Coordinate helpers ────────────────────────────────────
            // Zero-value points render at baseline (bottom grid line).
            // The baseline is the 0g grid line = chartBottom.
            fun valueToY(v: Float): Float =
                chartBottom - (v / maxValue) * chartHeight

            fun indexToX(i: Int): Float =
                chartLeft + (chartRight - chartLeft) * i / (n - 1).toFloat()

            // ── Grid lines (horizontal dashes) ────────────────────────
            // We draw exactly 3 horizontal grid lines matching the screenshot:
            // at 0 (baseline), 25%, 50%, 75% of maxValue → but visually the
            // screenshot shows lines at 0, 25, 50, 75 (for a 75g max).
            val gridSteps = 3   // lines at 0, max/3, 2*max/3, max  → 4 lines total
            val gridColor  = Color(0xFFCCCCCC)

            for (step in 0..gridSteps) {
                val v = maxValue * step / gridSteps
                val y = valueToY(v)
                drawLine(
                    color       = gridColor,
                    start       = Offset(chartLeft, y),
                    end         = Offset(chartRight, y),
                    strokeWidth = 1.5f,
                    pathEffect  = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                )
                // Y label to the left
                val labelText = "${v.toInt()}g"
                val lm = textMeasurer.measure(labelText)
                drawText(
                    textMeasurer = textMeasurer,
                    text         = labelText,
                    topLeft      = Offset(
                        x = chartLeft - lm.size.width - 8f,
                        y = y - lm.size.height / 2f
                    ),
                    style = TextStyle(fontSize = 13.sp, color = Color(0xFF999999))
                )
            }

            // ── Compute pixel offsets ─────────────────────────────────
            val offsets = points.mapIndexed { i, p ->
                Offset(indexToX(i), valueToY(p.value))
            }

            // ── Catmull-Rom smooth curve ──────────────────────────────
            val path = Path().apply { moveTo(offsets[0].x, offsets[0].y) }
            val tension = 0.35f
            for (i in 0 until n - 1) {
                val p0 = offsets[(i - 1).coerceAtLeast(0)]
                val p1 = offsets[i]
                val p2 = offsets[i + 1]
                val p3 = offsets[(i + 2).coerceAtMost(n - 1)]

                path.cubicTo(
                    p1.x + (p2.x - p0.x) * tension,
                    p1.y + (p2.y - p0.y) * tension,
                    p2.x - (p3.x - p1.x) * tension,
                    p2.y - (p3.y - p1.y) * tension,
                    p2.x, p2.y
                )
            }

            // ── Gradient fill ─────────────────────────────────────────
            val fillPath = Path().apply {
                addPath(path)
                lineTo(offsets.last().x, chartBottom)
                lineTo(offsets.first().x, chartBottom)
                close()
            }
            drawPath(
                path  = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.18f), Color.Transparent),
                    startY = chartTop,
                    endY   = chartBottom
                )
            )

            // ── Line ──────────────────────────────────────────────────
            drawPath(
                path  = path,
                color = lineColor,
                style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )

            // ── Dots + value labels ───────────────────────────────────
            val peakIndex = points.indexOfFirst { it.value == points.maxOf { p -> p.value } }

            offsets.forEachIndexed { i, pt ->
                val isLast  = i == n - 1
                val isZero  = points[i].value == 0f
                val dotColor = when {
                    isLast -> Color(0xFFCCCCCC)   // last dot greyed out (future)
                    else   -> lineColor
                }
                val dotRadius = if (i == peakIndex) 11f else 9f

                if (isZero || isLast) {
                    // Hollow circle — white fill + coloured stroke
                    drawCircle(color = Color.White,  radius = dotRadius,     center = pt)
                    drawCircle(color = dotColor,     radius = dotRadius,     center = pt,
                        style = Stroke(width = 2.5f))
                } else {
                    // Filled circle for points with data
                    drawCircle(color = dotColor, radius = dotRadius, center = pt)
                }

                // Value label above dot (only for non-zero)
                if (!isZero) {
                    val label   = "${points[i].value.toInt()}g"
                    val lm      = textMeasurer.measure(label)
                    val labelX  = (pt.x - lm.size.width / 2f)
                        .coerceIn(chartLeft, chartRight - lm.size.width)
                    val labelY  = pt.y - dotRadius - lm.size.height - 4f

                    drawText(
                        textMeasurer = textMeasurer,
                        text         = label,
                        topLeft      = Offset(labelX, labelY),
                        style        = TextStyle(
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color      = Color(0xFF222222)
                        )
                    )
                }

                // X-axis day label below the chart
                val dayLabel = points[i].label
                val dlm      = textMeasurer.measure(dayLabel)
                val dlX      = (pt.x - dlm.size.width / 2f)
                    .coerceIn(chartLeft, chartRight - dlm.size.width)
                drawText(
                    textMeasurer = textMeasurer,
                    text         = dayLabel,
                    topLeft      = Offset(dlX, chartBottom + 10f),
                    style        = TextStyle(
                        fontSize   = 12.sp,
                        color      = Color(0xFF888888),
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}