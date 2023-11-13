package com.example.stockmarketapp.presentation.stock_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockInfoViewModel @Inject constructor(
    private val repository: StockRepository,
    private val saveStateHandle: SavedStateHandle
): ViewModel() {

    var state by mutableStateOf(StockInfoState())

    init {
        viewModelScope.launch {
            val symbol = saveStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val stockInfoResult = async {  repository.getStockInfo(symbol) }
            val monthlyInfoResult = async {  repository.getMonthlyInfo(symbol) }

            when(val result = stockInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stock = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        stock = null
                    )
                }
                else -> Unit
            }

            when(val result = monthlyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        monthlyInfos = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                        stock = null
                    )
                }
                else -> Unit
            }
        }
    }
}