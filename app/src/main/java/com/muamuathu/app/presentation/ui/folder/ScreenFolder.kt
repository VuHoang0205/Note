package com.muamuathu.app.presentation.ui.folder

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.data.entity.Folder
import com.muamuathu.app.presentation.ui.folder.viewModel.FolderViewModel

@Composable
fun ScreenFolder() {
    val context = LocalContext.current
    val viewModel: FolderViewModel = hiltViewModel(context as ComponentActivity)
    val folderList by viewModel.folderListState
    val query by viewModel.query

    Content(folderList,
        query,
        onMenu = {},
        onSearch = {},
        onSearchFolder = {
            viewModel.searchFolder(it)
        },
        onEdit = {},
        onDelete = {},
        onItemFolder = {})
}

@Composable
private fun Content(
    folderList: List<Folder>,
    query: String,
    onMenu: () -> Unit,
    onSearch: () -> Unit,
    onSearchFolder: (query: String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onItemFolder: () -> Unit,
) {

    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(bottom = 46.dp)
        .background(colorResource(R.color.alice_blue))) {
        val (topView, searchView, contentView) = createRefs()
        Row(modifier = Modifier.fillMaxWidth().height(70.dp).background(Color.White)
            .padding(horizontal = 12.dp).constrainAs(topView) { top.linkTo(parent.top) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = {
                onMenu()
            }) {
                Image(painter = painterResource(R.drawable.ic_menu),
                    contentDescription = "menu")
            }
            val title = if (!folderList.isEmpty()) {
                String.format(stringResource(R.string.txt_folders) + " (%s)", folderList.size)
            } else {
                String.format(stringResource(R.string.txt_folders))
            }
            Text(
                text = title,
                color = colorResource(R.color.gulf_blue),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = {
                onSearch()
            }) {
                Image(painter = painterResource(R.drawable.ic_search),
                    contentDescription = "search")
            }
        }
        Column(modifier = Modifier.constrainAs(searchView) {
            top.linkTo(topView.bottom, 16.dp)
            start.linkTo(parent.start, 16.dp)
            end.linkTo(parent.end, 16.dp)
            width = Dimension.fillToConstraints
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 4.dp,
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = query,
                        onValueChange = { value ->
                            onSearchFolder(value)
                        },
                        label = {
                            Text(stringResource(R.string.txt_search_folder_name))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 14.sp),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(15.dp)
                                    .size(24.dp)
                            )
                        },
                        trailingIcon = {
                            if (!query.isEmpty()) {
                                IconButton(
                                    onClick = {
                                        onSearchFolder("")
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .size(24.dp)
                                    )
                                }
                            }
                        },
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

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)
            .constrainAs(contentView) {
                top.linkTo(searchView.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(folderList) { index, item ->
                ItemFolder(item, onItemFolder, onEdit, onDelete)
            }
        }
    }
}

@Composable
private fun ItemFolder(
    folder: Folder,
    onItemFolder: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth().height(75.dp)
        .background(shape = RoundedCornerShape(8.dp), color = Color.White).clickable {
            onItemFolder()
        }) {
        val (viewColor, iconFolder, cotentView, iconEdit, iconDelete) = createRefs()
        Image(painter = painterResource(R.drawable.ic_folder),
            contentDescription = "folder",
            modifier = Modifier.width(23.dp).height(19.dp).constrainAs(iconFolder) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(viewColor.end, 8.dp)
            })

        IconButton(onClick = {
            onEdit()
        }, modifier = Modifier.size(20.dp).constrainAs(iconEdit) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(iconDelete.start, 12.dp)
        }) {
            Image(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = "edit",
                colorFilter = ColorFilter.tint(colorResource(R.color.storm_grey))
            )
        }

        IconButton(onClick = {
            onDelete()
        }, modifier = Modifier.size(20.dp).constrainAs(iconDelete) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end, 16.dp)
        }) {
            Image(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = "delete",
                colorFilter = ColorFilter.tint(colorResource(R.color.storm_grey)),
            )
        }

        Column(modifier = Modifier.constrainAs(cotentView) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(iconFolder.end, 8.dp)
            end.linkTo(iconEdit.start, 8.dp)
            width = Dimension.fillToConstraints
        },
            verticalArrangement = Arrangement.spacedBy(4.dp)) {

            Text(
                text = folder.name,
                color = colorResource(R.color.gulf_blue),
                fontSize = 14.sp,
            )
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Journals: 7",
                    color = colorResource(R.color.storm_grey),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Todo: 5",
                    color = colorResource(R.color.storm_grey),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Divider(modifier = Modifier.width(4.dp)
            .clip(shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            .background(colorResource(R.color.royal_blue))
            .constrainAs(viewColor) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            })
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(emptyList(),
        "",
        onMenu = {},
        onSearch = {},
        onSearchFolder = {},
        onEdit = {},
        onDelete = {},
        onItemFolder = {})
}