package com.example.doevida.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarioView(
    currentMonth: YearMonth,
    onMonthChange: (YearMonth) -> Unit,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val monthDisplayName =
        currentMonth.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
            .replaceFirstChar { it.uppercase() }

    Column {
        // Cabeçalho
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Mês anterior", tint = Color(0xFF990410))
            }
            Text(text = "$monthDisplayName ${currentMonth.year}")
            IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Próximo mês", tint = Color(0xFF990410))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dias da semana
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("S", "T", "Q", "Q", "S", "S", "D").forEach { day ->
                Text(text = day, modifier = Modifier.width(40.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dias do mês
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfMonth = currentMonth.atDay(1).dayOfWeek.value % 7 // começa no Domingo
        val calendarDays = (1..daysInMonth).toList()

        Column {
            val totalCells = (daysInMonth + firstDayOfMonth + 6) / 7 * 7
            val dayCells = (0 until firstDayOfMonth).map { null } +
                    calendarDays +
                    (calendarDays.size + firstDayOfMonth until totalCells).map { null }
            val weeks = dayCells.chunked(7)

            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    week.forEach { day ->
                        if (day != null) {
                            val date = currentMonth.atDay(day)
                            val isSelected = date == selectedDate
                            val isPast = date.isBefore(LocalDate.now())
                            CelulaDia(
                                day = day,
                                isSelected = isSelected,
                                isEnabled = !isPast
                            ) {
                                if (!isPast) onDateSelected(date)
                            }
                        } else {
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun CelulaDia(
    day: Int,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF990410) else Color.Transparent
    val textColor = when {
        isSelected -> Color.White
        !isEnabled -> Color.Gray
        else -> Color.Black
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .clickable(enabled = isEnabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.toString(), color = textColor)
    }
}