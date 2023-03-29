package com.muamuathu.app.presentation.ui.note

import android.net.Uri
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.note.viewModel.AddNoteViewModel
import com.muamuathu.app.presentation.ui.note.viewModel.SelectFileViewModel


val EMPTY_IMAGE_URI: Uri = Uri.parse("file://journal/null")

enum class MediaType {
    IMAGE,
    VIDEO
}

@Composable
fun ScreenPickFile(mediaType: MediaType) {
    val context = LocalContext.current as ComponentActivity
    val eventHandler = initEventHandler()
    val noteViewModel = hiltViewModel<AddNoteViewModel>(context)
    val selectViewModel = hiltViewModel<SelectFileViewModel>(context)
    val coroutineScope = rememberCoroutineScope()
    Content(mediaType = mediaType, onImageUri = {
        if (!TextUtils.equals(it.toString(), EMPTY_IMAGE_URI.toString())) {
            coroutineScope.observeResultFlow(selectViewModel.getRealPathFromUri(it), {
                noteViewModel.updateAttachments(listOf(it))
            })
        }
        eventHandler.postNavEvent(NavEvent.PopBackStack())
    })
}

@Composable
private fun Content(
    mediaType: MediaType,
    onImageUri: (Uri) -> Unit,
) {

//    val accessMediaPermissionState = rememberPermissionState(
//        permission = Manifest.permission.ACCESS_MEDIA_LOCATION
//    )

//    LaunchedEffect(key1 = true, block = {
//        accessMediaPermissionState.launchPermissionRequest()
//    })
//
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI)
        }
    )

    LaunchedEffect(key1 = Unit, block = {
        when (mediaType) {
            MediaType.IMAGE -> launcher.launch("image/*")
            MediaType.VIDEO -> launcher.launch("video/*")
        }
    })
}

@Preview
@Composable
private fun PreviewContent() {
    Content(MediaType.IMAGE, {})
}



