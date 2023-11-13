package com.example.stockmarketapp.presentation.stock_name

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketapp.domain.model.StockNames

@Composable
fun StockItem (
    stock: StockNames,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = stock.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier =  Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
             Text(
                 text = "(${stock.symbol})",
                 fontWeight = FontWeight.Light,
                 color = MaterialTheme.colorScheme.onBackground
             )
        }
        Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stock.exchange,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onBackground,
            )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    StockItem(stock = StockNames(name = "name", exchange = "exchange", symbol = "symbol"))
}