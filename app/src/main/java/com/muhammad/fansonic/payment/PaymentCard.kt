package com.muhammad.fansonic.payment

import android.graphics.Paint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.muhammad.fansonic.R

@Composable
fun PaymentCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    username: TextFieldState,
    cardNumber: TextFieldState,
    expiryNumber: TextFieldState,
    cvcNumber: TextFieldState,
) {
    val density = LocalDensity.current.density
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }
    var cardWidth by remember { mutableFloatStateOf(0f) }
    var cardHeight by remember { mutableFloatStateOf(0f) }
    val backVisible by remember(cvcNumber.text) {
        derivedStateOf {
            isFlipped || (cvcNumber.text.isNotEmpty() && cvcNumber.text.length < 3)
        }
    }
    val cardType by remember(cardNumber.text) {
        derivedStateOf {
            if (cardNumber.text.length >= 8) CardType.VISA else CardType.NONE
        }
    }
    val cardContainerColor by animateColorAsState(
        targetValue = if (cardType == CardType.VISA) Color(0xFF1C478B) else Color(0xFF424242),
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "cardColor"
    )
    val flipRotation by animateFloatAsState(
        targetValue = if (backVisible) 180f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "flipRotation"
    )
    val animatedRotationX by animateFloatAsState(
        targetValue = rotationX, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "rotationX"
    )

    val animatedRotationY by animateFloatAsState(
        targetValue = rotationY, animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ), label = "rotationY"
    )
    Card(
        modifier = modifier
            .onSizeChanged{size ->
                cardWidth = size.width.toFloat()
                cardHeight = size.height.toFloat()
            }
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    isFlipped = !isFlipped
                })
            }
            .pointerInput(Unit){
                detectDragGestures(onDragEnd = {
                    rotationX = 0f
                    rotationY = 0f
                }, onDragCancel = {
                    rotationX = 0f
                    rotationY = 0f
                }, onDrag = {change, dragAmount ->
                    change.consume()
                    val x = change.position.x
                    val y = change.position.y
                    val normalizedX = (x - cardWidth / 2f) / (cardWidth / 2f)
                    val normalizedY = (y - cardHeight / 2f) / (cardHeight / 2f)
                    val maxRotation = 25f
                    rotationX = -normalizedY * maxRotation
                    rotationY = normalizedX * maxRotation
                })
            }
            .graphicsLayer {
                this.rotationY = flipRotation + animatedRotationY
                this.rotationX = animatedRotationX
                cameraDistance = 12f * density
            }
            .height(220.dp),
        shape = shape,
        elevation = CardDefaults.elevatedCardElevation(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardContainerColor)
    ) {
        if (flipRotation <= 90f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .drawWithContent {
                        val w = size.width
                        val h = size.height

                        drawCircle(
                            color = Color.White.copy(alpha = 0.08f),
                            radius = w * 0.6f,
                            center = Offset(w * 0.2f, h * 0.2f)
                        )

                        drawCircle(
                            color = Color.White.copy(alpha = 0.06f),
                            radius = w * 0.5f,
                            center = Offset(w * 0.85f, h * 0.1f)
                        )

                        drawCircle(
                            color = Color.White.copy(alpha = 0.05f),
                            radius = w * 0.7f,
                            center = Offset(w * 0.5f, h * 1.2f)
                        )

                        drawContent()
                    }
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.card_symbol),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopStart)
                )
                this@Card.AnimatedVisibility(
                    visible = cardType != CardType.NONE,
                    enter = fadeIn(), exit = fadeOut(), modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_visa_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    )
                }
                CardNumberVisual(
                    cardNumber = cardNumber.text.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    val color = if (username.text.isEmpty()) Color.White.copy(0.6f) else Color.White
                    val textAlign =
                        if (username.text.isEmpty()) TextAlign.Center else TextAlign.Left
                    Text(
                        text = stringResource(R.string.card_holder),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                    Text(
                        text = username.text.toString().ifEmpty { "----" },
                        modifier = Modifier.animateContentSize(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = color, fontWeight = FontWeight.Bold, textAlign = textAlign
                        )
                    )
                }
                Column(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val expiryDate = expiryNumber.text.toString()
                    val text = expiryDate.ifEmpty { "-- / --" }
                    val color = if (expiryDate.isEmpty()) Color.White.copy(0.6f) else Color.White
                    Text(
                        text = stringResource(R.string.expiry),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White
                        )
                    )
                    Text(
                        text = text,
                        modifier = Modifier.animateContentSize(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = color,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
        if (flipRotation > 90f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { this.rotationY = 180f },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.Black)
                )
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .defaultMinSize(minWidth = 60.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cvcNumber.text.toString().take(3),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                        modifier = Modifier
                            .animateContentSize(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec())
                            .padding(vertical = 4.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CardNumberVisual(
    cardNumber: String,
    modifier: Modifier = Modifier,
    showLastDigits: Boolean = true,
) {
    val context = LocalContext.current
    val lotoTypeface = ResourcesCompat.getFont(
        context, R.font.lato
    )
    val digits = cardNumber.filter { it.isDigit() }.take(16)
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        val total = 16
        val groupSize = 4
        val groups = total / groupSize

        val spacing = maxWidth / (total + (groups - 1))

        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = spacing.toPx() / 2.5f
            var x = spacing.toPx() / 2

            repeat(total) { index ->

                val hasDigit = index < digits.length

                val shouldShowDigit = showLastDigits && index >= 12 && hasDigit

                if (shouldShowDigit) {
                    drawContext.canvas.nativeCanvas.drawText(
                        digits[index].toString(),
                        x,
                        size.height / 2 + radius,
                        Paint().apply {
                            color = android.graphics.Color.WHITE
                            textAlign = Paint.Align.CENTER
                            textSize = radius * 3
                            typeface = lotoTypeface
                            isFakeBoldText = true
                        }
                    )
                } else {
                    drawCircle(
                        color = if (hasDigit) Color.White else Color.White.copy(alpha = 0.25f),
                        radius = radius,
                        center = Offset(x, size.height / 2)
                    )
                }

                x += spacing.toPx()

                if ((index + 1) % groupSize == 0 && index != total - 1) {
                    x += spacing.toPx()
                }
            }
        }
    }
}

@Preview
@Composable
private fun PaymentCardPreview() {
    PaymentCard(
        modifier = Modifier.fillMaxWidth(),
        username = TextFieldState("Muhammad Bilal"),
        cardNumber = TextFieldState("1234567812345678"),
        expiryNumber = TextFieldState("1226"),
        cvcNumber = TextFieldState()
    )
}