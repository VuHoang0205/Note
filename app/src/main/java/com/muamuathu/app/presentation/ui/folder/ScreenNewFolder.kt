package com.muamuathu.app.presentation.ui.folder

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.FolderColor
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.folder.viewModel.FolderViewModel
import com.muamuathu.app.presentation.ui.note.viewModel.AddNoteViewModel

@Composable
fun ScreenNewFolder(isChooseFolder: Boolean) {
    val eventHandler = initEventHandler()
    val viewModel: FolderViewModel = hiltViewModel()
    val addNoteViewModel: AddNoteViewModel = hiltViewModel(LocalContext.current as ComponentActivity)
    val colorList by remember { mutableStateOf(FolderColor.values().toList()) }
    val folderList by viewModel.entityFolderListState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (isChooseFolder) {
        LaunchedEffect(key1 = Unit, block = {
            viewModel.getFolderList()
        })
    }

    Content(
        colorList,
        folderList.list,
        onClose = {
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        },
        onAdd = { folderName, colorFolder ->
            val folder = Folder(name = folderName, color = colorFolder)
            coroutineScope.observeResultFlow(viewModel.saveFolder(folder))
        }, onItemSelect = {

        }, onDone = { folder ->
            if (isChooseFolder) {
                addNoteViewModel.updateFolder(folder)
            }
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        })
}

@Composable
private fun Content(
    colorList: List<FolderColor>,
    entityFolderList: List<Folder>,
    onClose: () -> Unit,
    onAdd: (folderName: String, colorFolder: Long) -> Unit,
    onItemSelect: (folder: Folder) -> Unit,
    onDone: (folder: Folder) -> Unit,
) {

    var folderName by remember { mutableStateOf("") }
    val stateVisibility by remember {
        derivedStateOf {
            folderName.isNotEmpty()
        }
    }

    var colorSelected by remember { mutableStateOf(FolderColor.ORANGE) }
    var folderSelected by remember { mutableStateOf(Folder()) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView) = createRefs()

        TopBarBase(title = stringResource(id = R.string.txt_add_new_folder),
            titleAlign = TextAlign.Center,
            navigationIcon = {
                IconButton(onClick = {
                    onClose()
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "close"
                    )
                }
            },
            listRightIcon = null,
            modifier = Modifier.constrainAs(topView) { top.linkTo(parent.top) })

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentView) {
                top.linkTo(topView.bottom, 40.dp)
                bottom.linkTo(parent.bottom, 30.dp)
                height = Dimension.fillToConstraints
            }) {
            val (imgPlus, textFieldInput, btnAdd, lazyRowColor, viewLine, lazyColumnFolder, btnDone) = createRefs()
            Image(painterResource(R.drawable.ic_plus),
                null,
                modifier = Modifier.constrainAs(imgPlus) {
                    top.linkTo(parent.top)
                    start.linkTo(lazyColumnFolder.start)
                })

            if (folderName.isNotEmpty()) {
                AnimatedVisibility(visible = stateVisibility, modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
                    .constrainAs(btnAdd) {
                        top.linkTo(imgPlus.top)
                        bottom.linkTo(imgPlus.bottom)
                        end.linkTo(lazyColumnFolder.end)
                    }) {
                    Button(
                        onClick = {
                            onAdd(folderName, colorSelected.color)
                            folderName = ""
                        }, shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(R.color.royal_blue))
                    ) {
                        Text(
                            text = stringResource(R.string.txt_add),
                            fontSize = 14.sp,
                            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            TextField(
                value = folderName,
                placeholder = {
                    Text(
                        stringResource(R.string.txt_enter_folder_name), color = colorResource(
                            R.color.storm_grey
                        )
                    )
                },
                onValueChange = {
                    folderName = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .constrainAs(textFieldInput) {
                        top.linkTo(imgPlus.top)
                        start.linkTo(imgPlus.end)
                        bottom.linkTo(imgPlus.bottom)
                        end.linkTo(btnAdd.start)
                        width = Dimension.fillToConstraints
                    },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(R.color.gulf_blue)
                ),
            )

            if (folderName.isNotEmpty()) {
                AnimatedVisibility(visible = stateVisibility,
                    modifier = Modifier.constrainAs(lazyRowColor) {
                        top.linkTo(btnAdd.bottom, 8.dp)
                        start.linkTo(imgPlus.start)
                        end.linkTo(btnAdd.end, 4.dp)
                        width = Dimension.fillToConstraints
                    }) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
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
                }
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.link_water))
                .constrainAs(viewLine) {
                    if (folderName.isEmpty()) {
                        top.linkTo(textFieldInput.bottom, 8.dp)
                    } else {
                        top.linkTo(lazyRowColor.bottom, 8.dp)
                    }
                })

            LazyColumn(
                modifier = Modifier.constrainAs(lazyColumnFolder) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(viewLine.bottom, 16.dp)
                    bottom.linkTo(btnDone.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(entityFolderList) { _, item ->
                    ItemFolder(
                        itemColor = item,
                        selected = folderSelected == item
                    ) {
                        folderSelected = item
                        onItemSelect(item)
                    }
                }
            }

            TextButton(
                enabled = folderSelected.name.isNotEmpty(),
                onClick = {
                    onDone(folderSelected)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .alpha(if (folderSelected.name.isNotEmpty()) 1f else 0.5f)
                    .background(
                        colorResource(R.color.royal_blue),
                        RoundedCornerShape(4.dp)
                    )
                    .constrainAs(btnDone) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(lazyColumnFolder.start)
                        end.linkTo(lazyColumnFolder.end)
                        width = Dimension.fillToConstraints
                    }) {
                Text(
                    text = stringResource(R.string.txt_done).toUpperCase(Locale.current),
                    color = Color.White,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
internal fun ItemFolderColor(
    itemColor: FolderColor,
    selected: Boolean,
    itemClick: (selectColor: FolderColor) -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable {
                itemClick(itemColor)
            }, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(CircleShape)
                .background(Color(itemColor.color))
        )
        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.ic_tick),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ItemFolder(
    itemColor: Folder,
    selected: Boolean,
    itemClick: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .clickable {
            itemClick()
        }) {
        val (imgFolder, boxColor, textName, checkBox) = createRefs()

        Image(painter = painterResource(id = R.drawable.ic_folder),
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(20.dp)
                .constrainAs(imgFolder) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })

        Box(modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(itemColor.color))
            .constrainAs(boxColor) {
                top.linkTo(imgFolder.top)
                bottom.linkTo(imgFolder.bottom)
                start.linkTo(imgFolder.end, 30.dp)
            })

        Text(text = itemColor.name,
            fontSize = 14.sp,
            color = colorResource(id = R.color.gulf_blue),
            modifier = Modifier.constrainAs(textName) {
                top.linkTo(imgFolder.top)
                bottom.linkTo(imgFolder.bottom)
                start.linkTo(boxColor.end, 8.dp)
                end.linkTo(checkBox.start)
                width = Dimension.fillToConstraints
            })
        RadioButton(selected = selected,
            modifier = Modifier.constrainAs(checkBox) {
                top.linkTo(imgFolder.top)
                end.linkTo(parent.end)
                bottom.linkTo(imgFolder.bottom)
            }, colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(R.color.royal_blue),
                unselectedColor = colorResource(R.color.storm_grey)
            ), onClick = {
                itemClick()
            })
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(emptyList(), emptyList(), {}, { _, _ -> }, {}, { })
}