package com.muhammad.fansonic.milky_way

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Immutable
data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val twinkleOffset: Float,
    val twinkleSpeed: Float,
    val color: Color,
    val cross: Boolean,
)

@Immutable
data class ShootingStar(
    val startX: Float,
    val startY: Float,
    val angle: Float,
    val length: Float,
    val speed: Float,
    var progress: Float = 0f,
)

@Immutable
data class Planet(
    val name: String,
    val xFraction: Float,
    val yFraction: Float,
    val radius: Float,
    val bodyColor: Color,
    val glowColor: Color,
    val hasRing: Boolean = false,
    val ringColor: Color = Color.Transparent,
    val ringTiltDeg: Float = 20f,
    val orbitSpeed: Float = 0f,
    val moonRadius: Float = 0f,
    val moonOrbitRadius: Float = 0f,
    val moonColor: Color = Color.Transparent,
    val stripeColors: List<Color> = emptyList(),
    val iceCapColor: Color = Color.Transparent,
    val spotColor: Color = Color.Transparent,
)

val SOLAR_SYSTEM_PLANETS = listOf(

    Planet(
        name = "Mercury",
        xFraction = 0.12f,
        yFraction = 0.15f,
        radius = 14f,
        bodyColor = Color(0xFF9C9C9C),
        glowColor = Color(0x228899AA)
    ),

    Planet(
        name = "Venus",
        xFraction = 0.32f,
        yFraction = 0.10f,
        radius = 20f,
        bodyColor = Color(0xFFE8C97A),
        glowColor = Color(0x33E8C97A),
        stripeColors = listOf(
            Color(0xFFD4AA50),
            Color(0xFFEEDD99)
        )
    ),

    Planet(
        name = "Earth",
        xFraction = 0.78f,
        yFraction = 0.14f,
        radius = 22f,
        bodyColor = Color(0xFF2A6FDB),
        glowColor = Color(0x332A6FDB),
        stripeColors = listOf(
            Color(0xFF3A8C40),
            Color(0xFF1A4FA0)
        ),
        moonRadius = 5f,
        moonOrbitRadius = 38f,
        moonColor = Color(0xFFCCCCCC),
        orbitSpeed = 1.8f
    ),

    Planet(
        name = "Mars",
        xFraction = 0.90f,
        yFraction = 0.28f,
        radius = 17f,
        bodyColor = Color(0xFFCC4422),
        glowColor = Color(0x33CC4422),
        stripeColors = listOf(
            Color(0xFFAA3311),
            Color(0xFFDD6644)
        ),
        iceCapColor = Color(0xCCFFFFFF)
    ),

    Planet(
        name = "Jupiter",
        xFraction = 0.18f,
        yFraction = 0.55f,
        radius = 46f,
        bodyColor = Color(0xFFD4A96A),
        glowColor = Color(0x44C8901A),
        stripeColors = listOf(
            Color(0xFFBB8844),
            Color(0xFFE8C890),
            Color(0xFFAA7733),
            Color(0xFFDDBB77),
            Color(0xFFCC9966)
        ),
        spotColor = Color(0xAACC3300),
        moonRadius = 4f,
        moonOrbitRadius = 68f,
        moonColor = Color(0xFFD4A96A),
        orbitSpeed = 2.5f
    ),

    Planet(
        name = "Saturn",
        xFraction = 0.55f,
        yFraction = 0.48f,
        radius = 38f,
        bodyColor = Color(0xFFE4C97A),
        glowColor = Color(0x33E4C97A),
        stripeColors = listOf(
            Color(0xFFD4A850),
            Color(0xFFEEDD99),
            Color(0xFFCC9933)
        ),
        hasRing = true,
        ringColor = Color(0xBBD4A850),
        ringTiltDeg = 18f
    ),

    Planet(
        name = "Uranus",
        xFraction = 0.90f,
        yFraction = 0.52f,
        radius = 28f,
        bodyColor = Color(0xFF7DCEE0),
        glowColor = Color(0x447DCEE0),
        stripeColors = listOf(
            Color(0xFF5DBBD0),
            Color(0xFF9DDFF0)
        ),
        hasRing = true,
        ringColor = Color(0x887DCEE0),
        ringTiltDeg = 75f
    ),

    Planet(
        name = "Neptune",
        xFraction = 0.70f,
        yFraction = 0.80f,
        radius = 26f,
        bodyColor = Color(0xFF2255BB),
        glowColor = Color(0x442255BB),
        stripeColors = listOf(
            Color(0xFF1A3FA0),
            Color(0xFF3366CC)
        ),
        spotColor = Color(0xBB4488FF),
        moonRadius = 5f,
        moonOrbitRadius = 46f,
        moonColor = Color(0xFF8899CC),
        orbitSpeed = 2.0f
    ),

    Planet(
        name = "Pluto",
        xFraction = 0.12f,
        yFraction = 0.85f,
        radius = 9f,
        bodyColor = Color(0xFFAA8866),
        glowColor = Color(0x22AA8866),
        iceCapColor = Color(0xCCEEDDCC)
    )
)


fun generateStars(count: Int): List<Star> {
    val random = Random(42)
    val colors = listOf(
        Color(0xFFFFFFFF), Color(0xFFCCDDFF),
        Color(0xFFFFEECC), Color(0xFFFFCCCC), Color(0xFFCCFFFF)
    )
    return List(count) {
        val t = random.nextFloat()
        val r = when {
            t < 0.70f -> random.nextFloat() * 1.2f + 0.5f
            t < 0.80f -> random.nextFloat() * 1.5f + 1.5f
            t < 0.90f -> random.nextFloat() * 2.0f + 3f
            t < 0.97f -> random.nextFloat() * 2.5f + 5f
            else -> random.nextFloat() * 2.5f + 5f
        }
        Star(
            x = random.nextFloat(),
            y = random.nextFloat(),
            radius = r,
            twinkleOffset = random.nextFloat() * 6.28f,
            twinkleSpeed = 0.6f + random.nextFloat() * 1.4f,
            color = colors.random(random),
            cross = r > 4.5f && random.nextFloat() > 0.4f
        )
    }
}

fun generateShootingStar(width: Float, height: Float): ShootingStar {
    return ShootingStar(
        startX = Random.nextFloat() * width,
        startY = Random.nextFloat() * height * 0.4f,
        angle = (20f + Random.nextFloat() * 35f) * (PI / 180f).toFloat(),
        length = width * (0.07f + Random.nextFloat() * 0.10f),
        speed = 0.003f + Random.nextFloat() * 0.004f
    )
}

fun DrawScope.drawRoundStar(star: Star, alpha: Float) {
    val x = star.x * size.width
    val y = star.y * size.height
    if (star.radius > 2.5f) {
        drawCircle(
            color = star.color.copy(alpha = alpha * 0.22f),
            radius = star.radius * 3.5f,
            center = Offset(x = x, y = y)
        )
        drawCircle(
            color = star.color.copy(alpha = alpha * 0.42f),
            radius = star.radius * 1.8f,
            center = Offset(x = x, y = y)
        )
    }
    drawCircle(
        color = star.color.copy(alpha = alpha),
        radius = star.radius,
        center = Offset(x = x, y = y)
    )
}

fun DrawScope.drawCrossStar(star: Star, alpha: Float) {
    val x = star.x * size.width
    val y = star.y * size.height
    drawCircle(
        color = star.color.copy(alpha = alpha * 0.18f),
        radius = star.radius * 4f,
        center = Offset(x = x, y = y)
    )
    drawCircle(
        color = star.color.copy(alpha = alpha),
        radius = star.radius * 0.6f,
        center = Offset(x = x, y = y)
    )
    val spikes = listOf(
        Triple(0f, star.radius * 4f, star.radius * 0.35f),
        Triple(90f, star.radius * 4f, star.radius * 0.35f),
        Triple(45f, star.radius * 2.2f, star.radius * 0.22f),
        Triple(135f, star.radius * 2.2f, star.radius * 0.22f),
    )
    spikes.forEach { (angle, length, width) ->
        rotate(angle, Offset(x, y)) {
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        star.color.copy(alpha = alpha * 0.7f),
                        star.color.copy(alpha = alpha),
                        star.color.copy(alpha = alpha * 0.7f),
                        Color.Transparent
                    ), startX = x - length, endX = x + length
                ),
                start = Offset(x = x - length, y),
                end = Offset(x = x + length, y),
                strokeWidth = width
            )
        }
    }
}


fun DrawScope.drawShootingStar(star: ShootingStar) {
    val tailP = (star.progress - 0.06f).coerceAtLeast(0f)
    val x1 = star.startX + cos(star.angle) * tailP * star.length * 200f
    val y1 = star.startY + sin(star.angle) * tailP * star.length * 200f
    val x2 = star.startX + cos(star.angle) * star.progress * star.length * 200f
    val y2 = star.startY + sin(star.angle) * star.progress * star.length * 200f
    drawLine(
        brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, Color(0xCCFFFFFF)),
        ),
        start = Offset(x1, y1),
        end = Offset(x2, y2), strokeWidth = 2.5f,
    )
}

fun DrawScope.drawPlanet(planet: Planet, moonAngle: Float) {
    val x = planet.xFraction * size.width
    val y = planet.yFraction * size.height
    val radius = planet.radius.dp.toPx()
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(planet.glowColor, Color.Transparent),
            center = Offset(x, y), radius = radius * 2.5f
        ), radius = radius * 2.5f, center = Offset(x, y)
    )
    if (planet.hasRing) {
        withTransform({
            rotate(planet.ringTiltDeg, Offset(x, y))
        }) {
            drawOval(
                color = planet.ringColor.copy(alpha = 0.45f),
                topLeft = Offset(x = x - radius * 2.1f, y = y - radius * 0.28f),
                size = Size(width = radius * 4.2f, height = radius * 0.56f),
                style = Stroke(width = radius * 0.15f)
            )
        }
    }
    drawCircle(color = planet.bodyColor, radius = radius, center = Offset(x = x, y = y))
    if (planet.stripeColors.isNotEmpty()) {
        val clipPath = Path().apply {
            addOval(
                Rect(left = x - radius, top = y - radius, right = x + radius, bottom = y + radius)
            )
        }
        clipPath(clipPath, ClipOp.Intersect) {
            val stripeHeight = (radius * 2f) / planet.stripeColors.size
            planet.stripeColors.forEachIndexed { index, color ->
                val top = y - radius + index * stripeHeight
                drawRect(
                    color = color.copy(alpha = 0.65f),
                    topLeft = Offset(x = x - radius, y = top),
                    size = Size(width = radius * 2f, height = stripeHeight)
                )
            }
        }
    }
    if (planet.iceCapColor != Color.Transparent) {
        drawCircle(
            color = planet.iceCapColor,
            radius = radius * 0.35f,
            center = Offset(x = x, y = y - radius * 0.68f)
        )
    }
    if (planet.spotColor != Color.Transparent) {
        drawOval(
            color = planet.spotColor,
            topLeft = Offset(x = x + radius * 0.1f, y = y + radius * 0.15f),
            size = Size(width = radius * 0.55f, height = radius * 0.33f)
        )
    }
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, Color(0x55000020)),
            center = Offset(x = x + radius * 0.5f, y = y),
            radius = radius * 1.1f
        ), radius = radius, center = Offset(x = x, y = y)
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x55FFFFFF), Color.Transparent),
            center = Offset(x = x - radius * 0.35f, y = y - radius * 0.35f),
            radius = radius * 0.55f
        ), center = Offset(x = x - radius * 0.35f, y = y - radius * 0.35f), radius = radius * 0.55f
    )
    if (planet.hasRing) {
        withTransform({
            rotate(planet.ringTiltDeg, Offset(x, y))
        }) {
            drawOval(
                color = planet.ringColor,
                topLeft = Offset(x = x - radius * 2.1f, y = y - radius * 0.28f),
                size = Size(width = radius * 4.2f, height = radius * 0.56f),
                style = Stroke(width = radius * 0.15f)
            )
        }
    }
    if (planet.moonRadius > 0f) {
        val moonRadius = planet.moonRadius.dp.toPx()
        val orbit = planet.moonOrbitRadius.dp.toPx()
        val mx = x + cos(moonAngle) * orbit
        val my = y + sin(moonAngle) * orbit * 0.4f
        drawCircle(
            color = planet.moonColor.copy(alpha = 0.3f),
            radius = moonRadius * 2f,
            center = Offset(x = mx, y = my)
        )
        drawCircle(
            color = planet.moonColor,
            radius = moonRadius,
            center = Offset(x = mx, y = my)
        )
    }
}

fun DrawScope.drawMilkyWay() {
    val width = size.width
    val height = size.height
    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color(0x08AABBFF),
                Color(0x12BBCCFF),
                Color(0x08AABBFF),
                Color.Transparent
            ), start = Offset(x = 0f, y = height * 0.3f), end = Offset(x = width, y = height * 0.7f)
        ), size = size
    )
}

@Composable
fun MilkyWayCanvas(
    modifier: Modifier = Modifier,
    starCount: Int = 350,
    showPlanets: Boolean = true,
    showShootingStar: Boolean = true,
    content: @Composable () -> Unit = {},
) {
    val stars = remember { generateStars(starCount) }
    val planets = remember { SOLAR_SYSTEM_PLANETS }
    val infiniteTransition = rememberInfiniteTransition(label = "space")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "time"
    )
    val orbitTime by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "orbit"
    )
    val shootingStars = remember { mutableStateListOf<ShootingStar>() }
    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(time) {
        if (showShootingStar && shootingStars.size < 3 && Random.nextFloat() < 0.015f) {
            shootingStars.add(generateShootingStar(width = canvasWidth, height = canvasHeight))
        }
        val iterator = shootingStars.iterator()
        while (iterator.hasNext()) {
            val star = iterator.next()
            star.progress += star.speed
            if (star.progress > 1.1f) iterator.remove()
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.matchParentSize()) {
            canvasWidth = size.width
            canvasHeight = size.height
            val width = size.width
            val height = size.height
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000008), Color(0xFF05020F),
                        Color(0xFF020818), Color(0xFF000005)
                    )
                ), size = size
            )
            drawMilkyWay()
            listOf(
                Triple(Offset(width * 0.25f, height * 0.3f), width * 0.45f, Color(0x0A3050FF)),
                Triple(Offset(width * 0.75f, height * 0.6f), width * 0.40f, Color(0x08602090)),
                Triple(Offset(width * 0.55f, height * 0.15f), width * 0.30f, Color(0x06204060))
            ).forEach { (center, radius, color) ->
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(color, Color.Transparent),
                        center = center, radius = radius
                    ), radius = radius, center = center
                )
            }
            stars.forEach { star ->
                val twinkle = 0.55f + 0.45f * sin(time * star.twinkleSpeed + star.twinkleOffset)
                if (star.cross) drawCrossStar(star, twinkle) else drawRoundStar(star, twinkle)
            }
            if(showPlanets){
                planets.forEach { planet ->
                    val moonAngle = orbitTime + planet.orbitSpeed
                    drawPlanet(planet = planet, moonAngle = moonAngle)
                }
            }
            shootingStars.forEach { drawShootingStar(it) }
        }
        content()
    }
}