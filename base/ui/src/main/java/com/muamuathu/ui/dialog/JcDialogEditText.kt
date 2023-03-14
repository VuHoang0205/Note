package com.muamuathu.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.muamuathu.ui.edittext.JcEditText

@Composable
fun JcDialogEditText(
    title: String,
    text: String,
    hint: String,
    negativeButtonText: String,
    positiveButtonText: String,
    onNegativeClicked: () -> Unit,
    onPositiveClicked: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var edtText by remember {
        mutableStateOf(text)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {

                Text(
                    modifier = Modifier,
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                JcEditText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(16.dp),
                    text = edtText,
                    hint = hint,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    onTextChanged = { edtText = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (negativeButtonText.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .clickable { onNegativeClicked() }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            text = negativeButtonText,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        modifier = Modifier
                            .clickable {
                                if (edtText.isNotEmpty()) {
                                    onPositiveClicked(edtText)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        text = positiveButtonText,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (edtText.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.5f
                        ),
                    )
                }
            }
        }
    }
}