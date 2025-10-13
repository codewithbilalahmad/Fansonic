package com.muhammad.fansonic.month_calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun MonthCalendarScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.Center
    ) {
        MonthCalendar(modifier = Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalTime::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonthCalendar(
    modifier: Modifier = Modifier,
    monthRange: IntRange = -100..100,
    initialDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = monthRange.count() / 2) { monthRange.count() }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    var selectedDate by remember { mutableStateOf(initialDate) }
    val currentMonthDate =
        initialDate.plus((currentPage - monthRange.count() / 2), DateTimeUnit.MONTH)
    val visibleMonthName =
        currentMonthDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val visibleYear = currentMonthDate.year
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedContent(
                targetState = "$visibleMonthName $visibleYear",
                transitionSpec = {
                    slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { -it } + fadeOut()
                },
                label = "monthTitle", modifier = Modifier.weight(1f)
            ) { title ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                },
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_backward),
                    contentDescription = null
                )
            }
            Spacer(Modifier.width(16.dp))
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_forward),
                    contentDescription = null
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, color = Color.Gray)
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val monthStart = initialDate.plus((page - monthRange.count() / 2), DateTimeUnit.MONTH)
            val monthDays = getMonthDays(monthStart)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                monthDays.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        week.forEach { day ->
                            val isSelected = selectedDate == day
                            val isToday = day == today
                            val isCurrentMonth = day.month == currentMonthDate.month
                            val borderColor by animateColorAsState(
                                targetValue = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isToday -> Color.Gray
                                    else -> Color.Transparent
                                },
                                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                label = "borderColor"
                            )
                            val borderWidth by animateDpAsState(
                                targetValue = if (isSelected || isToday) 2.dp else 0.dp,
                                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                                label = "borderWidth"
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .border(
                                        width = borderWidth,
                                        color = borderColor,
                                        shape = CircleShape
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        selectedDate = day
                                    }
                                    .padding(6.dp), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.day.toString(),
                                    style = MaterialTheme.typography.labelLarge.copy(color = if (isCurrentMonth) MaterialTheme.colorScheme.onBackground else Color.Gray, fontWeight = FontWeight.Medium)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun getMonthDays(date: LocalDate): List<LocalDate> {
    val firstOfMonth = LocalDate(date.year, date.month, 1)
    val firstDayOfWeek = firstOfMonth.dayOfWeek.ordinal
    val startOfCalendar = firstOfMonth.minus(firstDayOfWeek.toLong(), DateTimeUnit.DAY)
    val days = mutableListOf<LocalDate>()
    for (i in 0 until 35) {
        days.add(startOfCalendar.plus(i, DateTimeUnit.DAY))
    }
    return days
}