package com.muamuathu.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon
import com.muamuathu.ui.model.JcSelectionData

@Composable
internal fun JcSelectableItem(
    modifier: Modifier,
    item: JcSelectionData,
    isSelected: Boolean,
    onToggleSelection: (JcSelectionData) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onToggleSelection(item) }
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(start = 24.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.width(24.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = item.getTitle(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )

        if (isSelected) {
            JcIcon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp),
                iconSource = IconSource.Vector(Icons.Rounded.Check),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}