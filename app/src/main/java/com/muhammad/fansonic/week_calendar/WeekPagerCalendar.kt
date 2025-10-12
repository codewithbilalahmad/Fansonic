package com.muhammad.fansonic.week_calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.muhammad.fansonic.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun WeekPagerCalendarScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        WeekPagerCalendar()
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalTime::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun WeekPagerCalendar(
    modifier: Modifier = Modifier,
    weekRange: IntRange = -100..100,
    initialDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 100) { weekRange.count() }
    var selectedDate by remember { mutableStateOf(initialDate) }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val currentWeekStart = initialDate.plus((currentPage - 100), DateTimeUnit.WEEK)
    val visibleWeekDates = getWeekDatesFrom(currentWeekStart)
    val visibleMonthName =
        visibleWeekDates[3].month.name.lowercase().replaceFirstChar { it.uppercase() }
    val visibleYear = visibleWeekDates[3].year

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        val previousPage = pagerState.currentPage - 1
                        pagerState.animateScrollToPage(previousPage)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shapes = IconButtonDefaults.shapes(),
                modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize())
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_backward),
                    contentDescription = null,
                    modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize)
                )
            }
            AnimatedContent(targetState = "$visibleMonthName $visibleYear", transitionSpec = {
                slideInVertically { it } + fadeIn() togetherWith slideOutVertically { -it } + fadeOut()
            }, label = "monthYearAnimation") { monthYear ->
                Text(
                    text = monthYear,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
            IconButton(
                onClick = {
                    scope.launch {
                        val nextPage = pagerState.currentPage + 1
                        pagerState.animateScrollToPage(nextPage)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shapes = IconButtonDefaults.shapes(),
                modifier = Modifier.size(IconButtonDefaults.extraSmallContainerSize())
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    modifier = Modifier.size(IconButtonDefaults.extraSmallIconSize)
                )
            }
        }
        HorizontalPager(
            state = pagerState, beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val weekStart = initialDate.plus((page - 100), DateTimeUnit.WEEK)
            val dates = getWeekDatesFrom(weekStart)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                dates.forEach { date ->
                    val isSelected = date == selectedDate
                    val containerColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        label = "containerColor",
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                    )
                    val labelColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        label = "labelColor",
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (isSelected)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface,
                        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
                        label = "contentColor"
                    )
                    val shape =
                        if (isSelected) MaterialShapes.Cookie12Sided.toShape() else MaterialShapes.Square.toShape()
                    val name =
                        date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
                    val number = date.day
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(48.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { selectedDate = date }
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.labelLarge.copy(color = labelColor)
                        )
                        Spacer(Modifier.height(4.dp))
                        Surface(
                            color = containerColor,
                            shape = shape, modifier = Modifier.width(35.dp)
                        ) {
                            Text(
                                text = number.toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp),
                                color = contentColor,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

fun getWeekDatesFrom(date: LocalDate): List<LocalDate> {
    val startOfWeek = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
    return (0..6).map { day -> startOfWeek.plus(day, DateTimeUnit.DAY) }
}
