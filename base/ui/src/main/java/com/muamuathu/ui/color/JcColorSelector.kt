package com.muamuathu.ui.color

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.divider.JcVerticalDivider
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon

@Composable
fun JcColorSelector(
    modifier: Modifier,
    colors: List<Color>,
    color: Color,
    onColorSelected: (Color) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            JcColorItem(
                color = color,
                isChecked = true,
                onColorSelected = {

                }
            )
        }

        item {
            JcVerticalDivider(
                thickness = 1.dp,
                modifier = Modifier.height(36.dp)
            )
        }

        items(colors) {
            JcColorItem(
                color = it,
                isChecked = it == color,
                onColorSelected = onColorSelected
            )
        }
    }
}

@Composable
private fun JcColorItem(color: Color, isChecked: Boolean, onColorSelected: (Color) -> Unit) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onColorSelected(color) }
            .background(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    color = color,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = CircleShape
                )
        ) {
            if (isChecked) {
                JcIcon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    iconSource = IconSource.Vector(Icons.Rounded.Check),
                    tint = contentColorFor(backgroundColor = color)
                )
            }
        }
    }
}

@Preview
@Composable
private fun JcColorSelectorPreview() {
    var color by remember {
        mutableStateOf(Color.Red)
    }

    JcColorSelector(
        modifier = Modifier.fillMaxWidth(),
        colors = listOf(
            Color.Gray, Color.Red, Color.Green, Color.Yellow
        ),
        color = color,
        onColorSelected = {
            color = it
        }
    )
}