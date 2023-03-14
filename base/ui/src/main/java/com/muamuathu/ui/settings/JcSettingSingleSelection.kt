package com.muamuathu.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon
import com.muamuathu.ui.model.JcSelectionData

@Composable
fun JcSettingSingleSelection(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    dataSet: List<JcSelectionData>,
    currentSelection: JcSelectionData?,
    onToggleSelection: (JcSelectionData) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    JcSettingItem(
        modifier = modifier.clickable { expanded = !expanded },
        title = title,
        summary = summary,
        icon = icon,
        widget = {
            JcIcon(
                modifier = Modifier
                    .size(48.dp)
                    .padding(12.dp),
                iconSource = IconSource.Vector(
                    icon = if (expanded) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowRight
                ),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )

    if (expanded) {
        dataSet.forEach {
            JcSelectableItem(
                modifier = Modifier.fillMaxWidth(),
                item = it,
                isSelected = currentSelection == it,
                onToggleSelection = onToggleSelection
            )
        }
    }
}