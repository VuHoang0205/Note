package com.muamuathu.ui.bottombar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.JcIcon
import com.muamuathu.ui.model.JcMenuItem

@Composable
fun JcBottomBar(modifier: Modifier, items: List<JcMenuItem>, onClick: (JcMenuItem) -> Unit) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            items.forEach {
                JcBottomBarItem(
                    modifier = Modifier.weight(1f),
                    item = it,
                    onClick = onClick,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun JcBottomBarItem(
    modifier: Modifier,
    item: JcMenuItem,
    contentColor: Color,
    onClick: (JcMenuItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick(item) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
    ) {
        if (item.icon != null) {
            JcIcon(
                modifier = Modifier.size(24.dp),
                iconSource = item.icon,
                tint = contentColor
            )
        }

        if (item.text != null) {
            Text(
                text = item.text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }
    }
}