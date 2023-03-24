package com.muamuathu.app.presentation.ui.note

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.DEFAULT_ID
import com.muamuathu.app.domain.model.FileInfo
import com.muamuathu.app.presentation.components.topbar.Toolbar
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler

import com.muamuathu.app.presentation.ui.note.viewModel.SelectFileViewModel


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScreenSelectAttachment(isSelectImage: Boolean) {
    val storagePermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val context = LocalContext.current
    val eventHandler = initEventHandler()

    val viewModel: SelectFileViewModel = hiltViewModel(context as ComponentActivity)

    val mediaList by viewModel.bindMediaList().collectAsState(initial = mutableListOf())

    val mediaSelectedList = remember { mutableStateListOf<FileInfo>() }

    LaunchedEffect(key1 = true, block = {
        storagePermissionState.launchPermissionRequest()
    })

    if (storagePermissionState.permissionRequested) {
        viewModel.loadMediaFile(isSelectImage)
    }

    ContentUI(isSelectImage, storagePermissionState, mediaList, mediaSelectedList, onBack = {
        eventHandler.postNavEvent(NavEvent.PopBackStack(false))
    }, onSave = {}, onClickItem = {
        if (it.id != DEFAULT_ID) {
            if (mediaList.isNotEmpty()) {
                if (mediaSelectedList.contains(it)) {
                    mediaSelectedList.remove(it)
                } else {
                    mediaSelectedList.add(it)
                }
            } else {
                mediaSelectedList.add(it)
            }
        } else {

        }
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ContentUI(
    isImageMode: Boolean,
    storagePermissionState: PermissionState?,
    mediaList: MutableList<FileInfo>,
    mediaSelectedList: MutableList<FileInfo>,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onClickItem: (fileInfo: FileInfo) -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topView, contentView, contentPermission) = createRefs()

        Toolbar(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color.White)
            .padding(horizontal = 12.dp)
            .constrainAs(topView) { top.linkTo(parent.top) },
            title = if (isImageMode) R.string.select_pictures
            else R.string.select_videos,
            iconLeft = R.drawable.ic_close,
            onLeftClick = { onBack() },
            onRightClick = { onSave() },
            enableIconRight = mediaSelectedList.isNotEmpty())

        storagePermissionState?.let {
            if (storagePermissionState.hasPermission) {
                LazyVerticalGrid(columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(contentView) {
                            top.linkTo(topView.bottom)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        },
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(mediaList) {
                        var index = mediaSelectedList.indexOf(it)
                        index++
                        ItemMedia(
                            isImageMode,
                            it,
                            index,
                            index > 0) {
                            onClickItem(it)
                        }
                    }
                }
            } else if (storagePermissionState.permissionRequested) {
                Column(modifier = Modifier.padding(24.dp).constrainAs(contentPermission) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, horizontalAlignment = Alignment.CenterHorizontally) {
                    val textToShow = if (storagePermissionState.shouldShowRationale) {
                        stringResource(R.string.msg_storage_rationale_permission)
                    } else {
                        stringResource(R.string.msg_storage_permission)
                    }
                    Text(textToShow)
                    Button(onClick = { storagePermissionState.launchPermissionRequest() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(
                            R.color.royal_blue))) {
                        Text(stringResource(R.string.request_permission),
                            color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
private fun ContentPreview() {
    ContentUI(true, null, mutableListOf(), mutableListOf(), {}, {}, {})
}

@Composable
private fun ItemMedia(
    isImageMode: Boolean,
    fileInfo: FileInfo,
    position: Int = 0,
    isSelect: Boolean,
    onClickItem: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClickItem()
        }) {
        if (fileInfo.id != DEFAULT_ID) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(fileInfo.data)
                    .apply { decoderFactory(VideoFrameDecoder.Factory()) }
                    .build(),
                contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), contentScale = ContentScale.Crop)
        } else {
            Box(modifier = Modifier
                .background(colorResource(R.color.gainsboro))
                .fillMaxWidth()
                .aspectRatio(1f), contentAlignment = Alignment.Center) {
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(if (isImageMode) R.drawable.ic_camera else R.drawable.ic_video),
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }
        }

        if (isImageMode) {
            if (isSelect) {
                Text(modifier = Modifier.padding(6.dp).background(shape = CircleShape,
                    color = colorResource(R.color.royal_blue))
                    .padding(horizontal = if (position > 9) 6.dp else 9.dp, vertical = 4.dp)
                    .align(Alignment.BottomEnd),
                    text = position.toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            }
        } else {
            if (isSelect) {
                Text(modifier = Modifier.padding(6.dp).background(shape = CircleShape,
                    color = colorResource(R.color.royal_blue))
                    .padding(horizontal = if (position > 9) 6.dp else 9.dp, vertical = 4.dp)
                    .align(Alignment.TopStart),
                    text = position.toString(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)

            }
            Text(modifier = Modifier.padding(6.dp)
                .align(Alignment.BottomEnd),
                text = fileInfo.getDurationString(),
                fontSize = 13.sp,
                color = Color.White)
        }
    }
}
