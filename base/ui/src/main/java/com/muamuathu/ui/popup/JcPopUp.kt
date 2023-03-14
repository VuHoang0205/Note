package com.muamuathu.ui.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.muamuathu.ui.model.JcMenuItem

data class JcPopUpColors(
    val backgroundColor: Color = Color.White,
    val contentColor: Color = Color.Black
)

@Composable
fun JcPopUp(
    modifier: Modifier,
    alignment: Alignment = Alignment.BottomCenter,
    offset: IntOffset = IntOffset(0, 0),
    colors: JcPopUpColors = JcPopUpColors(),
    popUpItems: List<JcMenuItem>,
    onPopUpItemClicked: (JcMenuItem) -> Unit,
    onDismissRequest: () -> Unit
) {
    Popup(
        alignment = alignment,
        offset = offset,
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            modifier = modifier
                .defaultMinSize(minWidth = 140.dp)
                .width(240.dp)
                .background(
                    color = colors.backgroundColor,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            items(popUpItems) {
                JcPopUpItem(
                    item = it,
                    color = colors.contentColor,
                    onPopUpItemClicked = onPopUpItemClicked
                )
            }
        }
    }
}

@Composable
private fun JcPopUpItem(item: JcMenuItem, color: Color, onPopUpItemClicked: (JcMenuItem) -> Unit) {
    if (item.text != null) {
        Text(
            modifier = Modifier
                .clickable { onPopUpItemClicked(item) }
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 14.dp),
            text = item.text,
            textAlign = TextAlign.Start,
            color = color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}