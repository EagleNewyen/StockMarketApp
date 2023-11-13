package com.example.stockmarketapp.presentation.stock_name

sealed class StockNamesEvent {
    object Refresh: StockNamesEvent()
    data class OnSearchQueryChange(val query: String): StockNamesEvent()
    // data class Failure(val error: String): StockNamesEvent()
}
