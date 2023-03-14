package com.muamuathu.ui.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.model.JcMenuItem

@Composable
fun JcSegmentedButton(
    modifier: Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    containerSelectedColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    contentSelectedColor: Color = MaterialTheme.colorScheme.onPrimary,
    outlineColor: Color = MaterialTheme.colorScheme.outline,
    items: List<JcMenuItem>,
    currentItem: JcMenuItem,
    onItemSelected: (JcMenuItem) -> Unit
) {
    LazyRow(
        modifier = modifier
            .clip(
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = outlineColor,
                shape = MaterialTheme.shapes.small
            )
            .background(outlineColor)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items.forEach {
            item {
                JcSegmentedButtonItem(
                    item = it,
                    containerColor = containerColor,
                    containerSelectedColor = containerSelectedColor,
                    contentColor = contentColor,
                    contentSelectedColor = contentSelectedColor,
                    isSelected = it == currentItem,
                    onClicked = onItemSelected
                )
            }
        }
    }
}

@Composable
private fun JcSegmentedButtonItem(
    item: JcMenuItem,
    isSelected: Boolean,
    containerColor: Color,
    containerSelectedColor: Color,
    contentColor: Color,
    contentSelectedColor: Color,
    onClicked: (JcMenuItem) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClicked(item) }
            .height(36.dp)
            .background(
                color = if (isSelected) containerSelectedColor else containerColor
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.text.orEmpty(),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) contentSelectedColor else contentColor
        )
    }

}