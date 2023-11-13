package com.example.stockmarketapp.data.local



class StockDaoFake: StockDao {

    private var stockNamesEntity: MutableList<StockNamesEntity> = mutableListOf()

    override suspend fun insertStockName(stockNameEntity: List<StockNamesEntity>) {
        stockNamesEntity.addAll(stockNameEntity)
    }

    override suspend fun clearStockNames() {
         stockNamesEntity.clear()
    }

    override suspend fun searchStockNames(query: String): List<StockNamesEntity> {
        val lowercaseQuery = query.toLowerCase()
        return stockNamesEntity.filter { stockNamesEntity ->
            stockNamesEntity.name.toLowerCase().contains(lowercaseQuery) || stockNamesEntity.symbol.toUpperCase() == query
        }
    }
}