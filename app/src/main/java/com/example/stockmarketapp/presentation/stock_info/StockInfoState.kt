package com.example.stockmarketapp.presentation.stock_info

import com.example.stockmarketapp.domain.model.MonthlyInfo
import com.example.stockmarketapp.domain.model.StockInfo

data class StockInfoState(
    val monthlyInfos: List<MonthlyInfo> = emptyList(),
    val stock: StockInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
