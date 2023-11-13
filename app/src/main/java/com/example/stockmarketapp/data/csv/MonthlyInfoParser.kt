package com.example.stockmarketapp.data.csv

import com.example.stockmarketapp.data.mapper.toMonthlyInfo
import com.example.stockmarketapp.data.remote.dto.MonthlyInfoDto
import com.example.stockmarketapp.domain.model.MonthlyInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonthlyInfoParser @Inject constructor(): CSVParser<MonthlyInfo> {

    override suspend fun parse(stream: InputStream): List<MonthlyInfo> {

        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1)
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = MonthlyInfoDto(timestamp, close.toDouble())
                    dto.toMonthlyInfo()
                }
        }
            .also {
                csvReader.close()
            }
    }
}