package com.muhammad.fansonic.animations.components.ui

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassBlurCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    blurRadius: Dp = 20.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.onBackground,
    backgroundAlpha: Float = 0.30f,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val blurPx = with(density) { blurRadius.toPx() }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .animatedBorder(
                shape = RoundedCornerShape(cornerRadius),
                colors = listOf(Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta),
                borderWidth = 2.dp
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(cornerRadius))
                .then(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Modifier.graphicsLayer {
                            renderEffect = RenderEffect.createBlurEffect(
                                blurPx, blurPx, Shader.TileMode.CLAMP
                            ).asComposeRenderEffect()
                        }
                    } else {
                        Modifier
                    }
                )
                .background(
                    color = backgroundColor.copy(alpha = backgroundAlpha),
                    shape = RoundedCornerShape(cornerRadius)
                ))
        Box(modifier = modifier){
            content()
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun GlassBlurCardPreview() {
    Box(
        modifier = Modifier.background(Color.Black).padding(24.dp)
    ) {
        GlassBlurCard(modifier = Modifier.size(260.dp)) {
            Column(Modifier.padding(8.dp)){
                Text(
                    "✨ Glass Effect",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text ("Backdrop blur (Android 12+)", color = Color.White)
            }
        }
    }
}
