package com.example.stockmarketapp.di

import com.example.stockmarketapp.data.csv.CSVParser
import com.example.stockmarketapp.data.csv.MonthlyInfoParser
import com.example.stockmarketapp.data.csv.StockNameParser
import com.example.stockmarketapp.data.repository.StockRepositoryImpl
import com.example.stockmarketapp.domain.model.MonthlyInfo
import com.example.stockmarketapp.domain.model.StockNames
import com.example.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockNamesParser(
        stockNamesParser: StockNameParser
    ): CSVParser<StockNames>

    @Binds
    @Singleton
    abstract fun bindMonthlyInfoParser(
        monthlyInfoParser: MonthlyInfoParser
    ): CSVParser<MonthlyInfo>


    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ) : StockRepository
}