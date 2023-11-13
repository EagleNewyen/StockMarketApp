package com.example.stockmarketapp.presentation.stock_name

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmarketapp.presentation.destinations.StockInfoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination
@Composable


fun StocksScreen (
    navigator: DestinationsNavigator,
    viewModel: StockNamesViewModel = hiltViewModel()
) {


    val pullRefreshState = rememberPullRefreshState(
        refreshing = viewModel.state.isRefreshing,
        onRefresh = { viewModel.onEvent(StockNamesEvent.Refresh) }
    )

    val state = viewModel.state

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onEvent(StockNamesEvent.OnSearchQueryChange(query = it)) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .testTag("search"),
            placeholder = { Text(text = "Search...") },
            maxLines = 1,
            singleLine = true
        )
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.stocks.size) {  i ->
                    val stocks = state.stocks[i]
                    StockItem(
                        stock = stocks,
                        modifier = Modifier.fillMaxSize()
                            .clickable {
                                navigator.navigate(
                                    StockInfoScreenDestination(stocks.symbol)
                                )
                            }
                            .padding(16.dp)
                        )
                    if (i < state.stocks.size) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = viewModel.state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

    }

}