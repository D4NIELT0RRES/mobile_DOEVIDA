package com.example.doevida.util

import java.time.format.DateTimeFormatter

fun gerarHorarios(
    startTimeStr: String?,
    endTimeStr: String?
): List<String> {
    if (startTimeStr == null || endTimeStr == null) return emptyList()

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    // Ex.: "1970-01-01T08:00:00.000Z" -> pegar só "08:00"
    val startTime = java.time.LocalTime.parse(startTimeStr.substring(11, 16))
    val endTime = java.time.LocalTime.parse(endTimeStr.substring(11, 16))

    val times = mutableListOf<String>()
    var currentTime = startTime
    while (currentTime.isBefore(endTime)) {
        times.add(currentTime.format(timeFormatter))
        currentTime = currentTime.plusHours(1)
    }
    return times
}