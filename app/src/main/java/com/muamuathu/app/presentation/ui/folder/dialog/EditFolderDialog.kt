package com.muamuathu.app.presentation.ui.folder.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.FolderColor
import com.muamuathu.app.presentation.components.dialog.DialogBase
import com.muamuathu.app.presentation.event.DialogEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.folder.ItemFolderColor
import com.muamuathu.app.presentation.ui.folder.viewModel.FolderViewModel

@Composable
fun EditFolderDialog(
    folder: Folder,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
    onUpdateFolder: () -> Unit,
) {
    val eventHandler = initEventHandler()
    val viewModel: FolderViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    DialogBase(eventHandler = eventHandler, onDismissRequest, properties) {
        Content(
            folder = folder,
            onSave = { folder ->
                coroutineScope.observeResultFlow(viewModel.updateFolder(folder), successHandler = {
                    eventHandler.postDialogEvent(DialogEvent.None)
                    onUpdateFolder.invoke()
                })
            }
        ) { eventHandler.postDialogEvent(DialogEvent.None) }
    }
}

@Composable
private fun Content(
    folder: Folder,
    onSave: ((Folder) -> Unit)?,
    onCancel: () -> Unit,
) {
    val colorList by remember { mutableStateOf(FolderColor.values().toList()) }
    var folderName by remember { mutableStateOf(folder.name) }
    var colorSelected by remember { mutableStateOf(FolderColor.fromValue(folder.color)) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.alice_blue))
    ) {
        val (title, textField, textColor, lazyColor, btnCancel, btnSave) = createRefs()
        Text(text = stringResource(R.string.edit_folder),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = 16.dp)
                .constrainAs(title) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })

        Column(modifier = Modifier.constrainAs(textField) {
            top.linkTo(title.bottom, 20.dp)
            start.linkTo(parent.start, 16.dp)
            end.linkTo(parent.end, 16.dp)
            width = Dimension.fillToConstraints
        }) {

            Text(
                text = stringResource(R.string.name_of_folder),
                color = colorResource(id = R.color.storm_grey),
                fontSize = 14.sp
            )

            Card(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                elevation = 1.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            colorResource(id = R.color.storm_grey),
                            RoundedCornerShape(4.dp)
                        )
                ) {
                    TextField(
                        value = folderName,
                        onValueChange = { value ->
                            folderName = value
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(4.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            textColor = colorResource(R.color.storm_grey),
                            cursorColor = colorResource(R.color.storm_grey),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.color),
            color = colorResource(id = R.color.storm_grey),
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(textColor) {
                start.linkTo(textField.start)
                top.linkTo(textField.bottom, 20.dp)
            }
        )

        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(lazyColor) {
                    top.linkTo(textColor.bottom, 16.dp)
                }, horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(colorList) { _, item ->
                ItemFolderColor(
                    itemColor = item,
                    selected = colorSelected == item
                ) { item ->
                    colorSelected = item
                }
            }
        }

        val alpha = if (folderName.isEmpty()) 0.5f else 1f
        TextButton(modifier = Modifier
            .alpha(alpha)
            .constrainAs(btnSave) {
                end.linkTo(textField.end)
                top.linkTo(lazyColor.bottom, 16.dp)
            }, onClick = {
            onSave?.let {
                folder.name = folderName
                folder.color = colorSelected.color
                it(folder)
            }
        }, enabled = folderName.isNotEmpty()) {
            Text(
                text = stringResource(id = R.string.save),
                color = colorResource(id = R.color.royal_blue),
                fontSize = 17.sp
            )
        }

        TextButton(modifier = Modifier.constrainAs(btnCancel) {
            end.linkTo(btnSave.start, 24.dp)
            top.linkTo(btnSave.top)
            bottom.linkTo(btnSave.bottom)
        }, onClick = { onCancel() }) {
            Text(
                text = stringResource(id = R.string.cancel),
                color = colorResource(id = R.color.catalina_blue),
                fontSize = 17.sp
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(Folder(), null) {}
}