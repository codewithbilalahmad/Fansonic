package com.muhammad.fansonic.scroller

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// ─── Colors ───────────────────────────────────────────────────────────────────
private val ScrollerBg      = Color(0xFF2A2520)
private val ScrollerBgLight = Color(0xFF3A3530)
private val RidgeDark       = Color(0xFF1A1612)
private val Gold            = Color(0xFFE8D5A3)
private val GoldDim         = Color(0xFFB89D6A)
private val TickInactive    = Color(0xFF333333)
private val LockActive      = Color(0xFF5A4020)

private const val ZOOM_STEP = 0.1f
private const val DRAG_SENSITIVITY = 150f   // px of drag = full zoom range

@Composable
fun CameraZoomScrollerScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CameraZoomScroller(onZoomChanged = {})
    }
}

// ─── Main Composable ──────────────────────────────────────────────────────────
@Composable
fun CameraZoomScroller(
    onZoomChanged: (Float) -> Unit,         // call camera.cameraControl.setZoomRatio()
    modifier: Modifier = Modifier
) {
    val minZoom = 1f
    val maxZoom =  50f
    var zoom by remember { mutableFloatStateOf( 1f) }
    var isLocked by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    fun applyZoom(newZoom: Float) {
        if (isLocked) return
        val clamped = (newZoom.coerceIn(minZoom, maxZoom) * 10).roundToInt() / 10f
        if (clamped != zoom) {
            zoom = clamped
            onZoomChanged(clamped)
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Zoom Label ─────────────────────────────────────────────────────
        ZoomLabel(zoom = zoom, isLocked = isLocked)

        // ── Scroller Knob + Ticks ──────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            ScrollerKnob(
                isLocked = isLocked,
                onDrag = { dragDelta ->
                    // drag UP (negative delta) = zoom in
                    val range = maxZoom - minZoom
                    val zoomDelta = -(dragDelta / DRAG_SENSITIVITY) * range
                    applyZoom(zoom + zoomDelta)
                }
            )
            Spacer(Modifier.width(10.dp))
            TickTrack(
                zoom = zoom,
                minZoom = minZoom,
                maxZoom = maxZoom,
                tickCount = 20
            )
        }

        // ── Step Buttons + Lock ────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StepButton(label = "+") { applyZoom(zoom + ZOOM_STEP) }
            LockButton(isLocked = isLocked) { isLocked = !isLocked }
            StepButton(label = "−") { applyZoom(zoom - ZOOM_STEP) }
        }
    }
}

// ─── Zoom Label ───────────────────────────────────────────────────────────────
@Composable
private fun ZoomLabel(zoom: Float, isLocked: Boolean) {
    val color by animateColorAsState(
        targetValue = if (isLocked) Color(0xFF666666) else Gold,
        animationSpec = tween(200), label = "zoomLabelColor"
    )
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = "%.1f".format(zoom),
            color = color,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp
        )
        Text(
            text = "×",
            color = color.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

// ─── Scroller Knob ────────────────────────────────────────────────────────────
@Composable
private fun ScrollerKnob(
    isLocked: Boolean,
    onDrag: (Float) -> Unit
) {
    var isDragging by remember { mutableStateOf(false) }
    val glowAlpha by animateFloatAsState(
        targetValue = if (isDragging) 1f else 0f,
        animationSpec = tween(150), label = "glow"
    )

    Box(
        modifier = Modifier
            .width(68.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(34.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(ScrollerBgLight, ScrollerBg),
                    start = Offset(0f, 0f),
                    end = Offset(68f, 200f)
                )
            )
            .drawBehind {
                // Glow ring when dragging
                if (glowAlpha > 0f) {
                    drawRoundRect(
                        color = Gold.copy(alpha = glowAlpha * 0.6f),
                        size = size,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(34.dp.toPx()),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                }
            }
            .pointerInput(isLocked) {
                if (!isLocked) {
                    detectVerticalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onVerticalDrag = { _, dragAmount -> onDrag(dragAmount) }
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Ridged texture
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            repeat(16) { i ->
                val isEven = i % 2 == 0
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = if (isEven)
                                    listOf(RidgeDark, Color(0xFF4A4035), RidgeDark)
                                else
                                    listOf(Color(0xFF2E2820), Color(0xFF5A5040), Color(0xFF2E2820))
                            )
                        )
                )
            }
        }

        // Lock overlay
        if (isLocked) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(34.dp))
                    .background(Color.Black.copy(alpha = 0.45f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔒", fontSize = 22.sp)
            }
        }
    }
}

// ─── Tick Track ───────────────────────────────────────────────────────────────
@Composable
private fun TickTrack(
    zoom: Float,
    minZoom: Float,
    maxZoom: Float,
    tickCount: Int
) {
    val progress = ((zoom - minZoom) / (maxZoom - minZoom)).coerceIn(0f, 1f)

    Column(
        modifier = Modifier.height(200.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        // Reversed so top = max zoom
        (tickCount - 1 downTo 0).forEach { i ->
            val tickProgress = i.toFloat() / (tickCount - 1)
            val isMajor = i % 4 == 0
            val isActive = tickProgress <= progress
            val width: Dp = if (isMajor) 18.dp else 10.dp
            val color = when {
                isActive && isMajor -> Gold
                isActive            -> GoldDim
                else                -> TickInactive
            }
            Box(
                modifier = Modifier
                    .width(width)
                    .height(2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(color)
                    .then(
                        if (isActive && isMajor) Modifier.drawBehind {
                            drawRect(Gold.copy(alpha = 0.3f))
                        } else Modifier
                    )
            )
        }
    }
}

// ─── Step Button ──────────────────────────────────────────────────────────────
@Composable
private fun StepButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(ScrollerBg, ScrollerBgLight)
                )
            )
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, _ -> } // consume to avoid conflict
            },
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
            Text(label, color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Light)
        }
    }
}

// ─── Lock Button ──────────────────────────────────────────────────────────────
@Composable
private fun LockButton(isLocked: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (isLocked) LockActive else ScrollerBg,
        animationSpec = tween(200), label = "lockBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isLocked) Gold else Color(0xFF4A4035),
        animationSpec = tween(200), label = "lockBorder"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(bgColor)
            .drawBehind {
                drawCircle(
                    color = borderColor,
                    radius = size.minDimension / 2 - 1.dp.toPx(),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
                )
            },
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
            Text(if (isLocked) "🔒" else "🔓", fontSize = 18.sp)
        }
    }
}