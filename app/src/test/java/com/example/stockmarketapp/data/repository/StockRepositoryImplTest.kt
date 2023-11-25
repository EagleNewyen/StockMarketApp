package com.example.stockmarketapp.data.repository

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isEqualToIgnoringGivenProperties

import com.example.stockmarketapp.data.csv.MonthlyInfoParser
import com.example.stockmarketapp.data.csv.StockNameParser
import com.example.stockmarketapp.data.local.StockDaoFake
import com.example.stockmarketapp.data.local.StockDatabase
import com.example.stockmarketapp.data.local.StockNamesEntity
import com.example.stockmarketapp.data.remote.StockApi
import com.example.stockmarketapp.domain.model.StockNames
import com.example.stockmarketapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class StockRepositoryImplTest {
    private lateinit var repository: StockRepositoryImpl
    private lateinit var stockApi: StockApi
    private lateinit var stockDatabase: StockDatabase
    private lateinit var stockDao: StockDaoFake
    private lateinit var stockNameParser: StockNameParser
    private lateinit var monthlyInfoParser: MonthlyInfoParser


    @BeforeEach
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        stockApi = mockk(relaxed = true)
        stockDatabase = mockk(relaxed = true)
       // stockDao = StockDaoFake()
        //coEvery { stockDatabase.dao } returns stockDao
        stockNameParser = mockk(relaxed = true)
        monthlyInfoParser = mockk( relaxed = true)

        repository = StockRepositoryImpl(stockApi,stockDatabase,stockNameParser,monthlyInfoParser)
     }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }





    @Test
    fun `get stock names when fetchFromRemote is true`() = runBlocking {
        val testQuery = "testquery"
        val result = mutableListOf<Resource<List<StockNames>>>()
        repository.getStockNames(true, testQuery).collect{result.add(it)}

        assertThat(result.size).isEqualTo(4)



        val loadingState = result[0]
        val successState1 = result[1]
        val successState2 = result[2]
        val finalLoadingStateFalse = result[3]

        assertThat(loadingState).isEqualToIgnoringGivenProperties(Resource.Loading(true))
        assertThat(successState1).isEqualToIgnoringGivenProperties(Resource.Success(emptyList()))
        assertThat(successState2).isEqualToIgnoringGivenProperties(Resource.Success(emptyList()))
        assertThat(finalLoadingStateFalse).isEqualToIgnoringGivenProperties(Resource.Loading(false))



    }

    @Test
    fun `get stock names when fetchFromRemote is false && local data source is not empty`() = runBlocking {
        val testQuery = "testquery"
        val result = mutableListOf<Resource<List<StockNames>>>()
        repository.getStockNames(false, testQuery).collect{result.add(it)}

        assertThat(result.size).isEqualTo(3)

        val loadingState = result[0]
        val successState1 = result[1]
        val loadingStateFalse = result[2]


        assertThat(loadingState).isEqualToIgnoringGivenProperties(Resource.Loading(true))
        assertThat(successState1).isEqualToIgnoringGivenProperties(Resource.Success(emptyList()))
        assertThat(loadingStateFalse).isEqualToIgnoringGivenProperties(Resource.Loading(false))
    }



    @Test
    fun `Data stored in resourceSuccess`() = runBlocking {
        val testQuery = "Tesla"
        val mockData = listOf(StockNamesEntity(id = 1, name = "Tesla", exchange = "NYE", symbol = "TYSL"))

        coEvery { stockDatabase.dao.searchStockNames(testQuery) } returns mockData

        val result = mutableListOf<Resource<List<StockNames>>>()
        repository.getStockNames(false,testQuery).collect{result.add(it)}

        val success = result[1]

        assertThat(success.data?.size).isEqualTo(1)
        assertThat(success.data?.firstOrNull()?.name).isEqualTo("Tesla")
    }



    @Test
    fun `Api error returns IOException` () = runTest {
        val testQuery = "testquery"

        coEvery { stockApi.getStocks() } throws IOException("Network Error")

        repository.getStockNames(true, testQuery).test {
            awaitItem()
            awaitItem()
            val errorEmission = awaitItem()

            assertThat(errorEmission.message).isEqualTo("Cannot load data")
            cancelAndConsumeRemainingEvents()

        }




    }

    @Test
    fun `Api error returns HttpException` () = runTest {
        val testQuery = "testquery"

        coEvery { stockApi.getStocks() } throws HttpException(mockk(relaxed = true))

        repository.getStockNames(true, testQuery).test {
            awaitItem()
            awaitItem()
            val errorEmission = awaitItem()

            assertThat(errorEmission.message).isEqualTo("Invalid response, Cannot load data")
            cancelAndConsumeRemainingEvents()

        }

    }
}