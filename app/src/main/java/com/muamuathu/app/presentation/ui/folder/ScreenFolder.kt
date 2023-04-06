package com.muamuathu.app.presentation.ui.folder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.presentation.event.DialogEvent
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.ui.folder.dialog.EditFolderDialog
import com.muamuathu.app.presentation.ui.folder.viewModel.FolderViewModel
import com.muamuathu.app.presentation.ui.note.SearchView

@Composable
fun ScreenFolder() {
    val eventHandler = initEventHandler()
    val viewModel: FolderViewModel = hiltViewModel()
    val wrapperList by viewModel.entityFolderListState.collectAsState()

    @Composable
    fun loadData() {
        LaunchedEffect(key1 = Unit, block = {
            viewModel.getFolderList()
        })
    }

    fun reloadData() {
        viewModel.getFolderList()
    }

    loadData()

    Content(wrapperList.list,
        onAdd = {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.FolderAdd))
        },
        onSearch = {
            viewModel.searchFolder(it)
        },
        onEdit = {
            eventHandler.postDialogEvent(DialogEvent.Custom {
                EditFolderDialog(folder = it,
                    onUpdateFolder = {
                        reloadData()
                    })
            })
        },
        onDelete = {
            viewModel.deleteFolder(it)
        },
        onItemFolder = {

        })
}

@Composable
private fun Content(
    folderList: List<Folder>,
    onAdd: () -> Unit,
    onSearch: (query: String) -> Unit,
    onEdit: (Folder) -> Unit,
    onDelete: (Folder) -> Unit,
    onItemFolder: () -> Unit,
) {
    var query by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 46.dp)
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, searchView, contentView) = createRefs()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp)
                .constrainAs(topView) { top.linkTo(parent.top) },
        ) {
            IconButton(onClick = {
                onAdd()
            }, modifier = Modifier.align(Alignment.CenterStart)) {
                Image(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "menu"
                )
            }
            val title = if (folderList.isNotEmpty()) {
                String.format(stringResource(R.string.txt_folders) + " (%s)", folderList.size)
            } else {
                String.format(stringResource(R.string.txt_folders))
            }
            Text(
                text = title,
                color = colorResource(R.color.gulf_blue),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        SearchView(modifier = Modifier.constrainAs(searchView) {
            top.linkTo(topView.bottom, 16.dp)
            start.linkTo(parent.start, 16.dp)
            end.linkTo(parent.end, 16.dp)
            width = Dimension.fillToConstraints
        }, label = R.string.txt_search_folder_name, query = query, onSearch = {
            query = it
            onSearch(it)
        })

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(contentView) {
                    top.linkTo(searchView.bottom)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }, verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
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
    onEdit: (Folder) -> Unit,
    onDelete: (Folder) -> Unit,
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .background(shape = RoundedCornerShape(8.dp), color = Color.White)
        .clickable {
            onItemFolder()
        }) {
        val (viewColor, iconFolder, contentView, iconEdit, iconDelete) = createRefs()
        Image(painter = painterResource(R.drawable.ic_folder),
            contentDescription = "folder",
            modifier = Modifier
                .width(23.dp)
                .height(19.dp)
                .constrainAs(iconFolder) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(viewColor.end, 8.dp)
                })

        IconButton(onClick = {
            onEdit(folder)
        }, modifier = Modifier
            .size(20.dp)
            .constrainAs(iconEdit) {
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
            onDelete(folder)
        }, modifier = Modifier
            .size(20.dp)
            .constrainAs(iconDelete) {
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

        Column(
            modifier = Modifier.constrainAs(contentView) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(iconFolder.end, 8.dp)
                end.linkTo(iconEdit.start, 8.dp)
                width = Dimension.fillToConstraints
            },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = folder.name,
                color = colorResource(R.color.gulf_blue),
                fontSize = 14.sp,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Journals: " + folder.noteList.size,
                    color = colorResource(R.color.storm_grey),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Todo: " + folder.getTotalTask(),
                    color = colorResource(R.color.storm_grey),
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Divider(modifier = Modifier
            .width(4.dp)
            .clip(shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
            .background(Color(folder.color))
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
        onAdd = {},
        onSearch = {},
        onEdit = {},
        onDelete = {},
        onItemFolder = {})
}