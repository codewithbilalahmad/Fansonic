package com.muhammad.fansonic.depth_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

@Composable
fun ColorScreen() {
    val contentColor = getColorContentColor(Color(0xFF1976D2))
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) {
        Text(
            text = "Hello world",
            style = MaterialTheme.typography.headlineMedium.copy(color = contentColor)
        )
    }
}

@Composable
fun DepthCardScreen() {
    val colors = listOf(
        Color(0xFFFFCEB1),
        Color(0xFFD6E5BD),
        Color(0xFFF9E1A8),
        Color(0xFFBCD8EC),
        Color(0xFFDCCCEC),
        Color(0xFFFFDAB4),
        Color(0xFFFFCEB1),
        Color(0xFFD6E5BD),
        Color(0xFFF9E1A8),
        Color(0xFFBCD8EC),
        Color(0xFFDCCCEC),
        Color(0xFFFFDAB4),
        Color(0xFFFFCEB1),
        Color(0xFFD6E5BD),
        Color(0xFFF9E1A8),
        Color(0xFFBCD8EC),
        Color(0xFFDCCCEC),
        Color(0xFFFFDAB4),
        Color(0xFFFFCEB1),
        Color(0xFFD6E5BD),
        Color(0xFFF9E1A8),
        Color(0xFFBCD8EC),
        Color(0xFFDCCCEC),
        Color(0xFFFFDAB4),
        Color(0xFFFFCEB1),
        Color(0xFFD6E5BD),
        Color(0xFFF9E1A8),
        Color(0xFFBCD8EC),
        Color(0xFFDCCCEC),
        Color(0xFFFFDAB4),
        Color(0xFFFFCEB1),
        Color(0xFFD6E5BD),
        Color(0xFFF9E1A8),
        Color(0xFFBCD8EC),
        Color(0xFFDCCCEC),
        Color(0xFFFFDAB4),
    )
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(colors) { color ->
                DepthCard(color = color, image = R.drawable.pic1)
            }
        }
    }
}

@Composable
fun DepthCard(modifier: Modifier = Modifier, color: Color, image: Int) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(145.dp)
            .background(shape = RoundedCornerShape(16.dp), color = color),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .width(96.dp)
                .offset(y = (-24).dp)
                .fillMaxHeight(),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun getColorContentColor(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White
}