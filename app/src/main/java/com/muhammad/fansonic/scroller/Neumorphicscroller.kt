//package com.muhammad.fansonic.scroller
//
//
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.DecayAnimationSpec
//import androidx.compose.animation.core.exponentialDecay
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectVerticalDragGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.graphics.drawscope.clipRect
//import androidx.compose.ui.hapticfeedback.HapticFeedbackType
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.input.pointer.util.VelocityTracker
//import androidx.compose.ui.platform.LocalHapticFeedback
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//import kotlin.math.abs
//import kotlin.math.cos
//import kotlin.math.PI
//
//// ─── Rib spacing (pixels between each groove line) ───────────────────────────
//private const val RIB_SPACING = 28f
//
//// ─── How many pixels of drag = 0.1 zoom step ─────────────────────────────────
//private const val PIXELS_PER_STEP = 20f
//
///**
// * Improved NeumorphicScroller — feels like a real physical drum/barrel wheel.
// *
// * Improvements over original:
// *  1. **Perspective / barrel warp** — ribs drawn with varying alpha & vertical
// *     compression to simulate a cylindrical drum curving away at top & bottom.
// *  2. **Fling / momentum** — release with velocity and the wheel coasts to a stop
// *     via exponential decay, exactly like a physical dial.
// *  3. **Inertia snap** — after fling, the wheel snaps to the nearest clean 0.1
// *     zoom step so the label never shows a messy decimal.
// *  4. **Richer shading** — radial gradient overlay gives the drum a convex 3-D look.
// *  5. **Edge vignette** — top & bottom fade to background so ribs "disappear"
// *     around the drum's horizon, enhancing the cylindrical illusion.
// *  6. **Centre indicator** — a single bright hairline marks the active rib.
// *  7. **Haptic tick** — one lightweight tick per rib crossed, like a detent wheel.
// */
//@Composable
//fun NeumorphicScroller(
//    value: Float,
//    onValueChange: (Float) -> Unit,
//    range: ClosedFloatingPointRange<Float>,
//    modifier: Modifier = Modifier,
//    sensitivity: Float = 0.02f
//) {
//    val haptic = LocalHapticFeedback.current
//    val scope = rememberCoroutineScope()
//
//    // ── Visual rotation drives the rib animation (pixels, unbounded) ──────────
//    val visualRotation = remember { Animatable(0f) }
//
//    // ── Velocity tracker for fling ────────────────────────────────────────────
//    val velocityTracker = remember { VelocityTracker() }
//
//    // ── Decay spec for momentum scroll ───────────────────────────────────────
//    val decaySpec: DecayAnimationSpec<Float> = remember {
//        exponentialDecay(frictionMultiplier = 2.5f)
//    }
//
//    // ── Track which rib index we last fired haptic on ─────────────────────────
//    var lastHapticRib by remember { mutableFloatStateOf(0f) }
//
//    // ── Guard against re-entrancy between drag & external pinch zoom ──────────
//    var isDragging by remember { mutableStateOf(false) }
//    var lastExternalValue by remember { mutableFloatStateOf(value) }
//
//    // Sync visual rotation when zoom changes externally (e.g. pinch gesture)
//    LaunchedEffect(value) {
//        if (!isDragging && abs(value - lastExternalValue) > 0.001f) {
//            val targetRotation = (value - range.start) / sensitivity * (PIXELS_PER_STEP / 10f)
//            visualRotation.animateTo(targetRotation, tween(200))
//            lastExternalValue = value
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .clip(RoundedCornerShape(15.dp))
//            .background(theme.cameraBackground)
//            .neumorphic(
//                shape = RoundedCornerShape(15.dp),
//                isPressed = true,
//                elevation = 4.dp,
//                blurRadius = 1f,
//                theme = theme
//            )
//            .pointerInput(Unit) {
//                detectVerticalDragGestures(
//                    onDragStart = { offset ->
//                        isDragging = true
//                        lastExternalValue = value
//                        velocityTracker.resetTracking()
//                    },
//                    onDragEnd = {
//                        // ── Fling with momentum ───────────────────────────────
//                        val velocity = velocityTracker.calculateVelocity().y
//                        scope.launch {
//                            // Coast using exponential decay
//                            var lastRot = visualRotation.value
//                            visualRotation.animateDecay(
//                                initialVelocity = velocity * 0.4f,   // scale down raw velocity
//                                animationSpec = decaySpec
//                            ) {
//                                // During decay: apply zoom changes
//                                val delta = value - lastRot * sensitivity * (10f / PIXELS_PER_STEP)
//                                val newZoom = (value - (visualRotation.value - lastRot) *
//                                        sensitivity * (10f / PIXELS_PER_STEP))
//                                    .coerceIn(range)
//                                    .roundTo1Decimal()
//                                if (newZoom != value) onValueChange(newZoom)
//                                lastRot = visualRotation.value
//                            }
//
//                            // Snap to nearest clean 0.1 step
//                            val snappedZoom = value.roundTo1Decimal()
//                            if (snappedZoom != value) onValueChange(snappedZoom)
//
//                            lastExternalValue = value
//                            isDragging = false
//                        }
//                    },
//                    onDragCancel = {
//                        isDragging = false
//                        lastExternalValue = value
//                    }
//                ) { change, dragAmount ->
//                    change.consume()
//                    velocityTracker.addPosition(change.uptimeMillis, change.position)
//
//                    val newZoom = (value - dragAmount * sensitivity)
//                        .coerceIn(range)
//                        .roundTo1Decimal()
//
//                    // Haptic tick every time we cross a rib boundary
//                    val currentRib = (visualRotation.value / RIB_SPACING)
//                    if (abs(currentRib - lastHapticRib) >= 1f) {
//                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                        lastHapticRib = currentRib
//                    }
//
//                    // Edge bump haptic
//                    val atEdge = newZoom == range.start || newZoom == range.endInclusive
//                    if (atEdge && newZoom != value) {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                    }
//
//                    scope.launch {
//                        visualRotation.snapTo(visualRotation.value + dragAmount)
//                    }
//
//                    if (newZoom != value) onValueChange(newZoom)
//                }
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        val innerShape = RoundedCornerShape(6.dp)
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 8.dp)
//                .clip(innerShape)
//                .background(theme.cameraBackground)
//                .neumorphic(
//                    shape = innerShape,
//                    isPressed = false,
//                    theme = theme,
//                    elevation = 4.dp,
//                    blurRadius = 8f
//                )
//        ) {
//            Canvas(modifier = Modifier.fillMaxSize()) {
//                drawBarrelScrollWheel(
//                    rotation = visualRotation.value,
//                    darkShadow = theme.darkShadow,
//                    backgroundColor = theme.cameraBackground
//                )
//            }
//        }
//    }
//}
//
//// ─────────────────────────────────────────────────────────────────────────────
////  Barrel / drum wheel drawing
//// ─────────────────────────────────────────────────────────────────────────────
//
///**
// * Draws a cylindrical drum-style scroll wheel with:
// * - Perspective compression: ribs near top/bottom are squeezed closer together
// * - Alpha falloff: ribs fade out toward the top & bottom horizon
// * - Convex shading gradient overlaid on top to give 3-D roundness
// * - A bright centre indicator line
// * - Top/bottom vignette so ribs "wrap" behind the drum
// */
//private fun DrawScope.drawBarrelScrollWheel(
//    rotation: Float,
//    darkShadow: Color,
//    backgroundColor: Color,
//) {
//    val w = size.width
//    val h = size.height
//    val centerY = h / 2f
//
//    // How many ribs we need to fill the visible arc (slightly more than h/spacing)
//    val visibleRibCount = (h / RIB_SPACING).toInt() + 4
//
//    // The "drum radius" — controls how pronounced the perspective warp is
//    val drumRadius = h * 0.85f
//
//    clipRect {
//        // ── Draw ribs ────────────────────────────────────────────────────────
//        for (i in -visibleRibCount..visibleRibCount) {
//            // Linear position (flat)
//            val linearY = centerY + (i * RIB_SPACING) + (rotation % RIB_SPACING)
//
//            // Perspective: map linear Y → curved Y using cylindrical projection
//            val normalised = ((linearY - centerY) / drumRadius).coerceIn(-1f, 1f)
//            val curvedY = centerY + drumRadius * Math.sin(
//                normalised * (PI / 2.0)
//            ).toFloat()
//
//            // Alpha: full at centre, zero at top/bottom 15% of height
//            val distFromCenter = abs(curvedY - centerY) / (h / 2f)
//            val alpha = (1f - (distFromCenter / 0.85f).coerceIn(0f, 1f))
//                .let { it * it }   // square for smoother falloff
//
//            if (alpha < 0.02f) continue
//
//            // Stroke width: thicker at centre (more prominent), thinner at edges
//            val strokeWidth = (4f + 4f * (1f - distFromCenter)).coerceIn(2f, 8f)
//
//            // ── Shadow groove line ──────────────────────────────────────────
//            drawLine(
//                color = darkShadow.copy(alpha = alpha * 0.9f),
//                start = Offset(12f, curvedY),
//                end = Offset(w - 12f, curvedY),
//                strokeWidth = strokeWidth,
//                cap = StrokeCap.Round
//            )
//
//            // ── Highlight ridge line (offset 4px below groove) ──────────────
//            drawLine(
//                color = Color.White.copy(alpha = alpha * 0.55f),
//                start = Offset(12f, curvedY + strokeWidth * 0.6f),
//                end = Offset(w - 12f, curvedY + strokeWidth * 0.6f),
//                strokeWidth = strokeWidth * 0.5f,
//                cap = StrokeCap.Round
//            )
//        }
//
//        // ── Centre indicator — bright hairline showing the active position ───
//        drawLine(
//            color = Color.White.copy(alpha = 0.95f),
//            start = Offset(0f, centerY),
//            end = Offset(w, centerY),
//            strokeWidth = 1.5f,
//            cap = StrokeCap.Round
//        )
//        // Subtle amber accent on the indicator for camera-UI feel
//        drawLine(
//            color = Color(0xFFFFC060).copy(alpha = 0.55f),
//            start = Offset(w * 0.2f, centerY),
//            end = Offset(w * 0.8f, centerY),
//            strokeWidth = 2f,
//            cap = StrokeCap.Round
//        )
//
//        // ── Convex 3-D shading overlay ───────────────────────────────────────
//        // Left-to-right gradient: lighter on left/centre, darker on right edges
//        drawRect(
//            brush = Brush.horizontalGradient(
//                colors = listOf(
//                    Color.Black.copy(alpha = 0.18f),
//                    Color.Transparent,
//                    Color.Transparent,
//                    Color.Black.copy(alpha = 0.22f)
//                )
//            ),
//            size = Size(w, h)
//        )
//
//        // ── Top & bottom vignette — ribs "wrap" behind the drum horizon ──────
//        val vignetteHeight = h * 0.28f
//        drawRect(
//            brush = Brush.verticalGradient(
//                colors = listOf(backgroundColor, Color.Transparent),
//                startY = 0f,
//                endY = vignetteHeight
//            ),
//            size = Size(w, vignetteHeight)
//        )
//        drawRect(
//            brush = Brush.verticalGradient(
//                colors = listOf(Color.Transparent, backgroundColor),
//                startY = h - vignetteHeight,
//                endY = h
//            ),
//            topLeft = Offset(0f, h - vignetteHeight),
//            size = Size(w, vignetteHeight)
//        )
//    }
//}
//
//private fun Float.roundTo1Decimal(): Float =
//    (this * 10).let { kotlin.math.round(it) / 10f }