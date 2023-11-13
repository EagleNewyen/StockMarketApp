package com.example.stockmarketapp.presentation.stock_name

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarketapp.domain.repository.StockRepository
import com.example.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StockNamesViewModel @Inject constructor(
    private val repository: StockRepository,
): ViewModel() {

    var state by mutableStateOf(StockNamesState())

    private var searchJob: Job? = null

    init {
        getStockNames()
    }

    fun onEvent(event: StockNamesEvent) {
        when(event) {
            is StockNamesEvent.Refresh -> {
                getStockNames(_fetchFromRemote = true)
            }
            is StockNamesEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch() {
                    delay(500L)
                    getStockNames()
                }
            }
        }
    }

    fun getStockNames(
        _fetchFromRemote: Boolean = false,
        _query: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch() {
            repository.getStockNames(_fetchFromRemote, _query)
                .collect { result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { listStockNames ->
                                state = state.copy(
                                    stocks = listStockNames
                                )
                            }
                        }
                        is Resource.Error -> {
                            state = state.copy(
                                error = result.message
                            )
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }

                }
        }
    }

}