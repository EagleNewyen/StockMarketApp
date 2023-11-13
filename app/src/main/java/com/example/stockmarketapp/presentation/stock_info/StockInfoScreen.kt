package com.example.stockmarketapp.presentation.stock_info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.stockmarketapp.presentation.destinations.StockInfoScreenDestination.style
import com.example.stockmarketapp.ui.theme.DarkGrey
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination
fun  StockInfoScreen(
    symbol: String,
    viewModel: StockInfoViewModel = hiltViewModel()
) {
    var maxline by remember {
        mutableStateOf(false)
    }



    val state = viewModel.state
    if (state.error == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGrey)
                .padding(16.dp)
        ) {
        state.stock?.let { stock ->
            Text(
                text = stock.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stock.symbol,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.
                        fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Industry: ${stock.industry}",
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Country: ${stock.country}",
                fontStyle = FontStyle.Italic,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                modifier = Modifier.
                fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stock.description,
                maxLines =
                when (maxline) {
                    false -> 7
                    else -> Int.MAX_VALUE
                               },
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { maxline = !maxline }
            )
            Text(
                text = when (maxline) {
                    false -> "..show more"
                    else -> "..show less"
                                       },
                fontSize = 11.sp,
                modifier = Modifier
                    .clickable{ maxline = !maxline },

            )

            if (state.monthlyInfos.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Market Summary")
                Spacer(modifier = Modifier.height(32.dp))
                StockChart(
                    infos = state.monthlyInfos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .align(CenterHorizontally)
                )
            }
        }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

}