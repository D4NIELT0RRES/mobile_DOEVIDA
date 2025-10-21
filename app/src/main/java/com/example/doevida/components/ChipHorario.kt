package com.example.doevida.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChipHorario(
    time: String,
    isSelected: Boolean,
    onTimeSelected: (String) -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF990410) else Color.White
    val textColor = if (isSelected) Color.White else Color(0xFF990410)
    val borderColor = Color(0xFF990410)

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .clickable { onTimeSelected(time) }
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(text = time, color = textColor)
    }
}

