package com.muhammad.fansonic.calendar_topbar

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

@Composable
fun CalendarTopBarScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CalendarTopBar(onDateSelected = {})
    }
}

@Composable
fun CalendarTopBar(
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate) -> Unit,
    enabled: Boolean = true,
) {
    val dataSource = remember { CalendarDataSource() }
    var selectedDate by remember { mutableStateOf(dataSource.today) }
    var calendar = remember(selectedDate) { dataSource.getData(lastSelectedDate = selectedDate) }
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        WeekPager(
            calendar = calendar,
            enabled = enabled,
            onDateSelected = { date ->
                if (enabled) {
                    selectedDate = date
                    onDateSelected(date)
                }
            },
            onWeekChanged = { date ->
                selectedDate = date
                onDateSelected(date)
                calendar = dataSource.getData(startDate = date, lastSelectedDate = date)
            }
        )
    }
}

@SuppressLint("NewApi")
@Composable
fun WeekPager(
    calendar: Calendar,
    enabled: Boolean = true,
    onDateSelected: (LocalDate) -> Unit,
    onWeekChanged: (LocalDate) -> Unit,
) {
    val initialWeekStart = remember(calendar.selectedDate.date) {
        calendar.selectedDate.date.with(DayOfWeek.MONDAY)
    }
    val pageCount = 10000
    val initialIndex = pageCount / 2
    val pagerState = rememberPagerState(initialPage = initialIndex) { pageCount }
    var isInitial by remember { mutableStateOf(true) }
    LaunchedEffect(initialWeekStart) {
        pagerState.scrollToPage(initialIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        if (isInitial) {
            isInitial = false
            return@LaunchedEffect
        }
        val weekOffset = pagerState.currentPage - initialIndex
        val newWeekStart = initialWeekStart.plusWeeks(weekOffset.toLong())
        onWeekChanged(newWeekStart)
    }
    HorizontalPager(
        modifier = Modifier.fillMaxWidth(),
        state = pagerState,
        userScrollEnabled = enabled
    ) { page ->
        val weekOffset = page - initialIndex
        val weekStartDate = initialWeekStart.plusWeeks(weekOffset.toLong())
        WeekRow(
            startDate = weekStartDate,
            enabled = enabled,
            selectedDate = calendar.selectedDate.date,
            onDateSelected = onDateSelected
        )
    }
}

@SuppressLint("NewApi")
@Composable
fun WeekRow(
    startDate: LocalDate,
    selectedDates: List<LocalDate> = emptyList(),
    selectedDate: LocalDate,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    unSelectedTextColor: Color = MaterialTheme.colorScheme.onBackground,
    borderColor: Color = MaterialTheme.colorScheme.onBackground,
    selectedContainerColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    selectedItemWidth: Dp = 40.dp,
    horizontalPadding: Dp = 8.dp, onDateSelected: (LocalDate) -> Unit,
) {
    val weekDays = remember(startDate) {
        (0..6).map { startDate.plusDays(it.toLong()) }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        weekDays.forEach { date ->
            CalendarItem(
                date = Calendar.CalendarDate(
                    date = date,
                    isSelected = if (selectedDates.isEmpty()) date == selectedDate else date in selectedDates
                ),
                onClick = { onDateSelected(date) },
                enabled = enabled,
                selectedTextColor = selectedTextColor,
                selectedContainerColor = selectedContainerColor,
                unSelectedTextColor = unSelectedTextColor,
                itemWidth = selectedItemWidth, borderColor = borderColor
            )
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun CalendarItem(
    selectedContainerColor: Color,
    date: Calendar.CalendarDate,
    onClick: () -> Unit,
    enabled: Boolean = true,
    unSelectedTextColor: Color,
    selectedTextColor: Color,
    borderColor: Color,
    itemWidth: Dp = 40.dp,
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 6.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = if (date.isSelected) selectedContainerColor else borderColor,
                RoundedCornerShape(24.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (date.isSelected) selectedContainerColor else Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .padding(6.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.formattedDate,
                color = if (date.isSelected) selectedTextColor else unSelectedTextColor,
                fontSize = 10.sp
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                color = if (date.isSelected) selectedTextColor else unSelectedTextColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

@SuppressLint("NewApi")
class CalendarDataSource {
    val today: LocalDate get() = LocalDate.now()
    fun getData(startDate: LocalDate = today, lastSelectedDate: LocalDate): Calendar {
        val firstDayOfWeek = startDate.with(DayOfWeek.MONDAY)
        val endDayOfWeek = firstDayOfWeek.plusDays(7)
        val visibleDates = getDatesBetween(startDate = firstDayOfWeek, endDate = endDayOfWeek)
        return toModal(dateList = visibleDates, lastSelectedDate = lastSelectedDate)
    }

    private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream.iterate(startDate) { date ->
            date.plusDays(1)
        }.limit(numOfDays).collect(Collectors.toList())
    }

    private fun toModal(dateList: List<LocalDate>, lastSelectedDate: LocalDate): Calendar {
        return Calendar(
            selectedDate = toItem(date = lastSelectedDate, isSelectedDate = true),
            visibleDates = dateList.map { date ->
                toItem(date = date, isSelectedDate = date.isEqual(lastSelectedDate))
            }
        )
    }

    private fun toItem(date: LocalDate, isSelectedDate: Boolean): Calendar.CalendarDate {
        return Calendar.CalendarDate(
            isSelected = isSelectedDate,
            isToday = date.isEqual(today),
            date = date
        )
    }
}

@SuppressLint("NewApi")
data class Calendar(
    val selectedDate: CalendarDate,
    val visibleDates: List<CalendarDate>,
) {
    data class CalendarDate(
        val date: LocalDate,
        val isSelected: Boolean,
        val isToday: Boolean = date == LocalDate.now(),
    ) {
        val formattedDate = date.format(DateTimeFormatter.ofPattern("E"))
    }
}