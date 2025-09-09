package com.muhammad.fansonic

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AnimatedSelectionListScreen() {
    val items = listOf(
        "Jetpack Compose",
        "Kotlin",
        "Java",
        "Python",
        "C",
        "C++",
        "C#",
        "JavaScript",
        "TypeScript",
        "Go",
        "Rust",
        "Swift",
        "Objective-C",
        "Ruby",
        "PHP",
        "R",
        "Scala",
        "Dart",
        "Haskell",
        "Perl",
        "Elixir"
    )
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var targetIndex by remember { mutableIntStateOf(-1) }
    var isAnimating by remember { mutableStateOf(false) }
    val itemPositions = remember { mutableStateMapOf<Int, IntOffset>() }
    val borderOffset = remember { Animatable(IntOffset.Zero, IntOffset.VectorConverter) }
    val borderSize = remember { Animatable(IntSize.Zero, IntSize.VectorConverter) }
    val screenWidth = configuration.screenWidthDp.dp
    val itemWidthPx = with(density) { (screenWidth - 16.dp * 2).roundToPx() }
    val itemHeightPx = with(density) { 80.dp.roundToPx() }
    val targetSize = IntSize(itemWidthPx, itemHeightPx)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    LaunchedEffect(targetIndex) {
        if(targetIndex != -1 && targetIndex != selectedIndex){
            val from = itemPositions[selectedIndex]
            val to = itemPositions[targetIndex]
            if (from != null && to != null) {
                isAnimating = true
                borderOffset.snapTo(from)
                borderSize.snapTo(targetSize)
                launch { borderOffset.animateTo(to, tween(400)) }
                launch { borderSize.animateTo(targetSize, animationSpec = tween(400)) }
                delay(400L)
                selectedIndex = targetIndex
                isAnimating = false
            }
            selectedIndex = targetIndex
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Choose Language",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF4F4F4))
            )
        }, containerColor = Color(0xFFF4F4F4)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 32.dp), state = listState
            ) {
                itemsIndexed(items) { index, title ->
                    val isSelected = index == selectedIndex && !isAnimating
                    LanguageItem(
                        title = title,
                        isSelected = isSelected,
                        onClick = {
                            if(!isAnimating) targetIndex = index
                        }, onPositioned = {offset ->
                            itemPositions[index] = offset
                        })
                }
            }
            if (isAnimating) {
                Box(
                    modifier = Modifier
                        .offset {
                            borderOffset.value
                        }
                        .size(
                            width = with(density) { borderSize.value.width.toDp() },
                            height = with(density) { borderSize.value.height.toDp() }
                        )
                        .border(
                            width = 4.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(16.dp)
                        ))
            }

        }
    }
}

@Composable
fun LanguageItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onPositioned: (IntOffset) -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color(0xFFDDDDDD),
        animationSpec = tween(300),
        label = "borderColor"
    )
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                onPositioned(coordinates.positionInParent().round())
            },
        border = BorderStroke(
            width = if (isSelected) 4.dp else 2.dp,
            color = borderColor
        ),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )
    }
}