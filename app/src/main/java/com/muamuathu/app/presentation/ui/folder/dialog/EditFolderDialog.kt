package com.muamuathu.app.presentation.ui.folder.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.folder.ItemFolderColor
import com.muamuathu.app.presentation.ui.folder.viewModel.FolderViewModel

@Composable
fun EditFolderDialog(
    folder: Folder,
    onDismissRequest: () -> Unit = {},
    properties: DialogProperties = DialogProperties(),
) {
    val eventHandler = initEventHandler()
    val viewModel: FolderViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    DialogBase(eventHandler = eventHandler, onDismissRequest, properties) {
        Content(
            folder = folder,
            onSave = { folder ->
                coroutineScope.observeResultFlow(viewModel.updateFolder(folder), successHandler = {

                })
            }
        ) { onDismissRequest }
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
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (title, textField, lazyColor, btnCancel, btnSave) = createRefs()
        Text(text = stringResource(R.string.edit_folder), modifier = Modifier.constrainAs(title) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Column(modifier = Modifier.constrainAs(textField) {
            top.linkTo(title.bottom, 16.dp)
            start.linkTo(parent.start, 16.dp)
            end.linkTo(parent.end, 16.dp)
            width = Dimension.fillToConstraints
        }) {

            Text(text = stringResource(R.string.name_of_folder))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 1.dp,
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = folderName,
                        onValueChange = { value ->
                            folderName = value
                        },
                        label = {
                            androidx.compose.material.Text(stringResource(R.string.txt_search_folder_name))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(4.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            textColor = colorResource(R.color.storm_grey),
                            cursorColor = colorResource(R.color.storm_grey),
                            leadingIconColor = colorResource(R.color.storm_grey),
                            trailingIconColor = colorResource(R.color.storm_grey),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .constrainAs(lazyColor) {
                    top.linkTo(textField.bottom, 16.dp)
                }) {
            itemsIndexed(colorList) { _, item ->
                ItemFolderColor(
                    itemColor = item,
                    selected = colorSelected == item
                ) { item ->
                    colorSelected = item
                }
            }
        }

        TextButton(modifier = Modifier.constrainAs(btnSave) {
            end.linkTo(textField.end)
            top.linkTo(lazyColor.bottom, 16.dp)
        }, onClick = {
            onSave?.let {
                folder.name = folderName
                folder.color = colorSelected.color
                it(folder)
            }
        }) {
            Text(text = stringResource(id = R.string.save))
        }

        TextButton(modifier = Modifier.constrainAs(btnSave) {
            end.linkTo(btnSave.start, 24.dp)
            top.linkTo(btnSave.top)
            bottom.linkTo(btnSave.bottom)
        }, onClick = { onCancel() }) {
            Text(text = stringResource(id = R.string.cancel))
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content(Folder(), null, {})
}