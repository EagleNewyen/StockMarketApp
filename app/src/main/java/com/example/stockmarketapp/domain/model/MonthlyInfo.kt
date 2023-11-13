package com.example.stockmarketapp.domain.model

import java.time.LocalDate

data class MonthlyInfo (
    val date: LocalDate,
    val close: Double
        )