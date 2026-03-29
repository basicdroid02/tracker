package com.example.tracker.ui.utils

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.verticalScrollbar(
    scrollState: ScrollState,
    color: Color = Color(0xFFFF4081).copy(alpha = 0.7f)
): Modifier = this.drawWithContent {

    drawContent()

    val height = size.height
    val contentHeight = scrollState.maxValue + height

    if (contentHeight > height) {

        val scrollbarHeight = (height / contentHeight) * height
        val scrollbarOffsetY = (scrollState.value / contentHeight) * height

        val thickness = 6.dp.toPx()
        val sidePadding = 4.dp.toPx()

        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = size.width - thickness - sidePadding,
                y = scrollbarOffsetY
            ),
            size = Size(thickness, scrollbarHeight),
            cornerRadius = CornerRadius(12f, 12f)
        )
    }
}