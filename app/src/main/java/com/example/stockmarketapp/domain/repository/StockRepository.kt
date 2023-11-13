package com.example.stockmarketapp.domain.repository

import com.example.stockmarketapp.domain.model.MonthlyInfo
import com.example.stockmarketapp.domain.model.StockInfo
import com.example.stockmarketapp.domain.model.StockNames
import com.example.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getStockNames(
        fetchFromRemote: Boolean,
        query: String,
    ): Flow<Resource<List<StockNames>>>

    suspend fun getMonthlyInfo(
        symbol: String
    ): Resource<List<MonthlyInfo>>

    suspend fun getStockInfo(
        symbol: String
    ): Resource<StockInfo>

}