package com.example.stockmarketapp.data.remote

import com.example.stockmarketapp.data.remote.dto.StockInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getStocks(
        @Query("apikey") apiKey: String = API_KEY
    ) : ResponseBody


    @GET("query?function=TIME_SERIES_MONTHLY&datatype=csv")
    suspend fun getMonthlyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): ResponseBody


     @GET("query?function=OVERVIEW")
     suspend fun getStockInfo(
         @Query("symbol") symbol: String,
         @Query("apikey") apiKey: String = API_KEY
     ) : StockInfoDto





    companion object {
        const val API_KEY = "L8Z1FQXQ6A5AFL36"
        const val BASE_URL = "https://alphavantage.co"
    }
}