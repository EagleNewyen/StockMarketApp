package com.example.stockmarketapp.presentation.stock_name

import com.example.stockmarketapp.domain.model.StockNames

data class StockNamesState(
    val stocks: List<StockNames> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)
