package com.example.stockmarketapp.data.mapper

import com.example.stockmarketapp.data.remote.dto.MonthlyInfoDto
import com.example.stockmarketapp.domain.model.MonthlyInfo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun MonthlyInfoDto.toMonthlyInfo(): MonthlyInfo {
    val pattern = "yyyy-MM-dd"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDate = LocalDate.parse(timestamp, formatter)
    return MonthlyInfo(
        date = localDate,
        close = close
    )
}