package com.muamuathu.ui.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.model.JcMenuItem

@Composable
internal fun JcToolbarMenuItem(item: JcMenuItem, onMenuItemClicked: (JcMenuItem) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onMenuItemClicked(item) }
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.icon != null) {
            when (item.icon) {
                is IconSource.IconResourceSource -> Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    painter = painterResource(id = item.icon.icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                is IconSource.Vector -> Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(12.dp),
                    imageVector = item.icon.icon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}