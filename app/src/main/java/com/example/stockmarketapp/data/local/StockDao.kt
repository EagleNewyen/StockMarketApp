package com.example.stockmarketapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockName(
        stockNameEntity: List<StockNamesEntity>
    )

    @Query("DELETE FROM stocknamesentity")
    suspend fun clearStockNames()

    @Query(
        """
            SELECT *
            FROM stocknamesentity
            WHERE LOWER (name) LIKE '%' || LOWER (:query) || '%' OR
                UPPER(:query) == symbol
        """
    )
    suspend fun searchStockNames(query: String): List<StockNamesEntity>
}