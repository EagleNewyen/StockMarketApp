package com.example.stockmarketapp.domain.model


data class StockInfo (
    val symbol: String,
    val name: String,
    val description: String,
    val country: String,
    val industry: String,
)