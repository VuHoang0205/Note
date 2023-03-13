package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon

@Composable
fun JcSettingItem(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    widget: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (icon != null) {
            JcIcon(
                modifier = Modifier.size(24.dp),
                iconSource = icon,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (!summary.isNullOrEmpty()) {
                Text(
                    text = summary,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        widget()
    }
}