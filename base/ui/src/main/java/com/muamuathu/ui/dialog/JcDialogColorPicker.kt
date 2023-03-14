package com.muamuathu.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.*

@Composable
fun JcDialogColorPicker(
    title: String,
    negativeButtonText: String,
    positiveButtonText: String,
    onNegativeClicked: () -> Unit,
    onPositiveClicked: () -> Unit,
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val controller = rememberColorPickerController()

    var color by remember { mutableStateOf(controller.selectedColor.value) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                HsvColorPicker(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .aspectRatio(1f),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        color = colorEnvelope.color
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(horizontal = 24.dp),
                    controller = controller
                )

                Spacer(modifier = Modifier.height(16.dp))

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .padding(horizontal = 24.dp),
                    controller = controller
                )

                Spacer(modifier = Modifier.height(16.dp))

                Spacer(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            color = color,
                            shape = MaterialTheme.shapes.small
                        )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .clickable { onNegativeClicked() }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        text = negativeButtonText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        modifier = Modifier
                            .clickable {
                                onColorSelected(color)
                                onPositiveClicked()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        text = positiveButtonText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}