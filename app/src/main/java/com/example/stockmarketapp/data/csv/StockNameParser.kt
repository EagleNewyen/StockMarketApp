package com.example.stockmarketapp.data.csv

import com.example.stockmarketapp.domain.model.StockNames
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockNameParser @Inject constructor(): CSVParser<StockNames> {

    override suspend fun parse(stream: InputStream): List<StockNames> {

        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    StockNames(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null,
                    )
                }
        }
            .also {
                csvReader.close()
            }
    }
}