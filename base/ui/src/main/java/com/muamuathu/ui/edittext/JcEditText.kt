package com.muamuathu.ui.edittext

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun JcEditText(
    modifier: Modifier,
    text: String,
    hint: String,
    color: Color,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    onTextChanged: (String) -> Unit = {}
) {
    var focus by remember {
        mutableStateOf(false)
    }
    BasicTextField(
        modifier = modifier.onFocusChanged {
            focus = it.isFocused
        },
        value = text,
        onValueChange = onTextChanged,
        decorationBox = { innerTextField ->
            if (text.isNotEmpty() || focus) {
                innerTextField()
            } else {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = color.copy(alpha = 0.4f)
                    )
                )
            }
        },
        cursorBrush = SolidColor(color),
        textStyle = style.copy(color = color)
    )
}