package com.muhammad.fansonic

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun CodingSessions() {
    val sessionNames = listOf(
        "Kotlin",
        "Jetpack Compose",
        "Java",
        "Python",
        "C++",
        "C#",
        "JavaScript",
        "TypeScript",
        "Swift",
        "Go",
    )
    val hours = (0..23).map { hour -> String.format("%02d:00", hour) }
    val sessionTasks = listOf(
        "Coding",
        "Debugging",
        "Practice",
        "Code Review",
        "Refactoring",
        "Pair Programming",
        "Testing"
    )
    val sessionColors = listOf(
        Color(0xFFEF5350), Color(0xFF26A69A), Color(0xFF7E57C2),
        Color(0xFF42A5F5), Color(0xFFFFB74D), Color(0xFFD32F2F),
        Color(0xFF66BB6A), Color(0xFFBA68C8), Color(0xFF5C6BC0),
        Color(0xFF90A4AE)
    )
    val sessionLessons = remember {
       List(sessionNames.size){index ->
           val lessonHours = hours.shuffled().take((10..18).random())
           lessonHours.associateWith { hour ->
               if(hour in lessonHours){
                   Lesson(
                       title = "${sessionNames[index]} - $hour",
                       task = sessionTasks.random()
                   )
               } else null
           }
       }
    }
    val hourHeaderListState = rememberLazyListState()
    val sessionListState = remember { List(sessionNames.size) { LazyListState() } }
    LaunchedEffect(Unit) {
        snapshotFlow { hourHeaderListState.firstVisibleItemIndex to hourHeaderListState.firstVisibleItemScrollOffset }.collectLatest { (index, offset) ->
            sessionListState.forEach { listState ->
                if (listState.firstVisibleItemIndex != index || listState.firstVisibleItemScrollOffset != offset) {
                    listState.scrollToItem(index, offset)
                }
            }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Today's Coding Lessons",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black,
                titleContentColor = Color.White
            )
        )
    }, containerColor = Color.Black) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            stickyHeader {
                HourHeader(hours = hours, state = hourHeaderListState)
            }
            items(sessionNames.size) { index ->
                val sessionName = sessionNames[index]
                val sessionColor = sessionColors[index]
                val lessons = sessionLessons[index]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(sessionColor, CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = sessionName,
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
                        )
                    }
                    LazyRow(
                        state = sessionListState[index],
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    ) {
                        items(hours.size) { index ->
                            val hour = hours[index]
                            val lesson = lessons[hour]
                            if (lesson != null) {
                                CodingLessonCard(
                                    title = lesson.title,
                                    time = hour,
                                    task = lesson.task,
                                    color = sessionColor
                                )
                            } else {
                                EmptyLessonCard(time = hour)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HourHeader(hours: List<String>, state: LazyListState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = "Hours",
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        LazyRow(
            state = state,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(hours) { hour ->
                HourCard(hour = hour)
            }
        }
    }
}

@Composable
fun CodingLessonCard(title: String, time: String, task: String, color: Color) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
            )
            Text(text = time, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
            Text(
                text = task,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
        }
    }
}

@Composable
fun EmptyLessonCard(time: String) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Composable
fun HourCard(hour: String) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = hour,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White)
            )
        }
    }
}

@Immutable
data class Lesson(val title: String, val task: String)

