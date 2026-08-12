package com.currentdetection.ui.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp

@Composable
fun SimpleBarChart(data: List<Float>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) return

    val maxData = data.maxOrNull() ?: 1f
    val barColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurfaceVariant

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barSpacing = canvasWidth / (data.size * 2)
            val barWidth = barSpacing

            // Draw X Axis
            drawLine(
                color = axisColor,
                start = Offset(0f, canvasHeight),
                end = Offset(canvasWidth, canvasHeight),
                strokeWidth = 2f
            )

            // Draw Bars
            data.forEachIndexed { index, value ->
                val barHeight = if (maxData > 0) (value / maxData) * (canvasHeight - 20f) else 0f
                val xOffset = index * (barWidth + barSpacing) + barSpacing / 2

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(xOffset, canvasHeight - barHeight),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx())
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceAround) {
            val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            labels.forEach { Text(it, style = MaterialTheme.typography.labelSmall) }
        }
    }
}
