package com.muhammad.fansonic.awesome

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.muhammad.fansonic.R

@Composable
fun AwesomeScreen() {
    val screenWidth = 400.dp
    val listState = rememberLazyListState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            cardItems.forEachIndexed { index, item ->
                CardItemDesign(
                    modifier = Modifier
                        .width(180.dp)
                        .aspectRatio(1f / 1.5f)
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 1.9f)
                            val scrollOffset = listState.scrollIndicatorState?.scrollOffset ?: 0
                            val fraction = 360f / cardItems.size
                            val step = screenWidth.toPx() / fraction
                            val offset = if (scrollOffset < step) 0f else scrollOffset / step
                            rotationZ = -(offset + (fraction * index)) % 360f
                        }
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 1f)
                            cameraDistance = 20f
                            rotationX = 320f
                        }, card = item
                )
            }
            LazyRow(state = listState, modifier = Modifier.fillMaxSize()) {
                items(cardItems.size, key = {it}) {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .width(screenWidth)
                        .background(Color.Transparent))
                }
            }
        }
    }
}

@Immutable
data class CardItem(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: Int,
    val gradient: List<Color>,
    val tag: String,
)

val cardItems = listOf(
    CardItem(
        "Abstract Motion",
        "FLUID DESIGN",
        "Experience the seamless transition of colors and shapes in a digital space.",
        R.drawable.ic_dark,
        listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)),
        "FEATURED"
    ),
    CardItem(
        "Cyber Vibe",
        "FUTURE TECH",
        "A glimpse into the neon-lit corridors of tomorrow's architecture.",
        R.drawable.ic_dark,
        listOf(Color(0xFF00F260), Color(0xFF0575E6)),
        "NEW"
    ),
    CardItem(
        "Golden Ratio",
        "MATHEMATICS",
        "The perfect balance between nature and geometric precision.",
        R.drawable.ic_dark,
        listOf(Color(0xFFF2994A), Color(0xFFF2C94C)),
        "PREMIUM"
    ),
    CardItem(
        "Deep Ocean",
        "QUIET WAVES",
        "Find peace in the silent depths of the blue abyss.",
        R.drawable.ic_dark,
        listOf(Color(0xFF2193b0), Color(0xFF6dd5ed)),
        "CALM"
    ),
    CardItem(
        "Urban Jungle",
        "CITY LIFE",
        "Where concrete structures meet the untamed growth of nature.",
        R.drawable.ic_dark,
        listOf(Color(0xFFed213a), Color(0xFF93291e)),
        "TRENDING"
    ),
    CardItem(
        "Cosmic Nebula",
        "ASTRONOMY",
        "Explore the deep mysteries of the universe and distant galaxies.",
        R.drawable.ic_dark,
        listOf(Color(0xFF8E2DE2), Color(0xFFFDC830)),
        "EXPLORE"
    ),
    CardItem(
        "Volcanic Core",
        "GEOLOGY",
        "Feel the raw power and heat radiating from the earth's center.",
        R.drawable.ic_dark,
        listOf(Color(0xFF00C853), Color(0xFFFFD600)),
        "HOT"
    ),
    CardItem(
        "Arctic Frost",
        "CLIMATE",
        "The serene beauty of frozen landscapes and crystal clear ice.",
        R.drawable.ic_dark,
        listOf(Color(0xFF00B8D4), Color(0xFFAA00FF)),
        "FRESH"
    )
)

@Composable
fun CardItemDesign(modifier: Modifier = Modifier, card: CardItem) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(card.gradient))
                .drawBehind {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.12f),
                        radius = size.width * 0.45f,
                        center = center.copy(
                            x = size.width * 0.9f, y = size.height * 0.1f
                        )
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.12f),
                        radius = size.width * 0.65f,
                        center = center.copy(
                            x = size.width * 0.15f, y = size.height * 0.95f
                        )
                    )
                }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.border(
                            1.dp,
                            Color.White.copy(alpha = 0.4f),
                            RoundedCornerShape(12.dp)
                        )
                    ) {
                        Text(
                            text = card.tag,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(
                        imageVector = ImageVector.vectorResource(card.icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Column {
                    Text(
                        text = card.subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        letterSpacing = 2.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 32.sp,
                            lineHeight = 38.sp
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = card.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.75f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}