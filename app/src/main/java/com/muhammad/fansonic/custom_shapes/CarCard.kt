package com.muhammad.fansonic.custom_shapes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

@Composable
@Preview
fun CarCard() {
    val radius = with(LocalDensity.current) { 60.dp.toPx() }
    val cornerRadiusDp = 80.dp
    val cornerRadius = with(LocalDensity.current) { cornerRadiusDp.toPx() }

    val padding = 8.dp
    val shape = remember {
        GenericShape { size, _ ->
            val width = size.width
            val height = size.height

            // Start from top-left
            moveTo(0f, radius)
            quadraticTo(0f, 0f, radius, 0f)

            // Top edge → Top-End cut
            lineTo(width - cornerRadius - radius, 0f)
            quadraticTo(width - cornerRadius, 0f, width - cornerRadius, radius)

            quadraticTo(
                width - cornerRadius,
                cornerRadius,
                width - radius,
                cornerRadius
            )

            quadraticTo(width, cornerRadius, width, cornerRadius + radius)

            // Right side
            lineTo(width, height - radius)
            quadraticTo(width, height, width - radius, height)

            // Bottom edge → Bottom-Start cut (mirror of top-end)
            lineTo(cornerRadius + radius, height)

            quadraticTo(
                cornerRadius,
                height,
                cornerRadius,
                height - radius
            )

            quadraticTo(
                cornerRadius,
                height - cornerRadius,
                radius,
                height - cornerRadius
            )

            quadraticTo(0f, height - cornerRadius, 0f, height - cornerRadius - radius)

            // Left side back to start
            lineTo(0f, radius)
        }
    }
    Box(modifier = Modifier.size(500.dp)) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
        }

        Box(
            modifier = Modifier
                .size(cornerRadiusDp - padding)
                .align(Alignment.TopEnd)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(cornerRadiusDp - padding)
                .align(Alignment.BottomStart)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_dark),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}