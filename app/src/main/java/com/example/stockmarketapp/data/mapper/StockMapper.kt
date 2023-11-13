package com.example.stockmarketapp.data.mapper

import com.example.stockmarketapp.data.local.StockNamesEntity
import com.example.stockmarketapp.data.remote.dto.StockInfoDto
import com.example.stockmarketapp.domain.model.StockInfo
import com.example.stockmarketapp.domain.model.StockNames

fun StockNamesEntity.toStockName(): StockNames {
    return StockNames(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun StockNames.toStockNameEntity(): StockNamesEntity {
    return StockNamesEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun StockInfoDto.toStockInfo(): StockInfo {
    return StockInfo(
        symbol = symbol?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: "",
    )
}

