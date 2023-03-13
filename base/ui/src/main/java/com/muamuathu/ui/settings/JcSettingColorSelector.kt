package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.color.JcColorSelector
import com.muamuathu.ui.icon.IconSource

@Composable
fun JcSettingColorSelector(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    colors: List<Color>,
    color: Color,
    onColorSelected: (Color) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        JcSettingItem(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            summary = summary,
            icon = icon
        )

        JcColorSelector(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = colors,
            color = color,
            onColorSelected = onColorSelected
        )
    }

}