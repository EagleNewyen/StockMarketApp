package com.example.stockmarketapp.presentation.stock_info

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockmarketapp.domain.model.MonthlyInfo

import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    infos: List<MonthlyInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = remember(infos) {
        (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }
    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign  = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    // X axis
    Canvas(modifier = modifier) {
        val spacePerMonth = (size.width - spacing) / 4.4f
        val spacePer = (size.width) / 6f
        (0..4).forEach { i ->
            val info = infos[i]
            val month = DateTimeFormatter.ofPattern("MMM", Locale.getDefault()).format(info.date)
            val year = DateTimeFormatter.ofPattern("yy").format(info.date)
            // info.date.year = full year
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    ("$month $year'"),
                    // spacing + i * spacePerMonth,
                    size.width - spacing - i * size.width / 5f,
                    size.height - 5,
                    textPaint
                )
            }
        }
        // Y axis
        val priceStep = (upperValue - lowerValue ) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * i).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }
        // Actual Graph/Path line
        var old = 0f
        var x2 = 0f
        val strokePath = Path().apply {
            val height = size.height
            val width = size.width
            // size.width - spacing - i * size.width / 5f,
            (0..4).forEach { i ->
                val info = infos[i]
                val nextInfo = infos.getOrNull(i+1) ?: infos.last()
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                old = spacing + i * spacePerMonth
                val x1 = width - spacing - i * spacePer
                val y1 = height - spacing - (leftRatio * height).toFloat()
                x2 = width - spacing - (i+1) * spacePer
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y1)
                }
                val lastX = (x1 + x2) / 2f
                // curve math
                quadraticBezierTo(
                    x1, y1, lastX, (y1 + y2) / 2f
                )
            }
        }
// Gradient
        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(x2, size.height - spacing)
                lineTo(old, size.height - spacing)
                close()
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}