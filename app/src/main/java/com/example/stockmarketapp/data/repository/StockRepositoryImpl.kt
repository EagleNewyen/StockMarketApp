package com.example.stockmarketapp.data.repository

import com.example.stockmarketapp.data.csv.CSVParser
import com.example.stockmarketapp.data.csv.MonthlyInfoParser
import com.example.stockmarketapp.data.local.StockDatabase
import com.example.stockmarketapp.data.mapper.toStockInfo
import com.example.stockmarketapp.data.mapper.toStockName
import com.example.stockmarketapp.data.mapper.toStockNameEntity
import com.example.stockmarketapp.data.remote.StockApi
import com.example.stockmarketapp.domain.model.MonthlyInfo
import com.example.stockmarketapp.domain.model.StockInfo
import com.example.stockmarketapp.domain.model.StockNames
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
     private val api: StockApi,
     private val db: StockDatabase,
     private val stockNameParser: CSVParser<StockNames>,
     private val monthlyInfoParser: CSVParser<MonthlyInfo>
): StockRepository {

    private val dao = db.dao


    override suspend fun getStockNames(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<StockNames>>> {
        return flow {

            emit(Resource.Loading(true))
            val localStockNames = dao.searchStockNames(query)

            emit(Resource.Success(
                data = localStockNames.map { it.toStockName() }
            ))

            val isDbEmpty = localStockNames.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteStocknames = try {
                val response = api.getStocks()
                stockNameParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Cannot load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Invalid response, Cannot load data"))
                null
            }
            remoteStocknames?.let { listStockNames ->
                dao.clearStockNames()
                dao.insertStockName(
                    listStockNames.map { it.toStockNameEntity() }
                )
                emit(Resource.Success(
                    data = dao
                        .searchStockNames("")
                        .map { it.toStockName() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getMonthlyInfo(symbol: String): Resource<List<MonthlyInfo>> {
        return try {
            val response = api.getMonthlyInfo(symbol)
            val result = monthlyInfoParser.parse(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load monthly intraday info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load monthly intraday info")

        }
    }

    override suspend fun getStockInfo(symbol: String): Resource<StockInfo> {
        return try {
            val result = api.getStockInfo(symbol)
            Resource.Success(result.toStockInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load Stock info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Couldn't load Stock info")

        }    }
}