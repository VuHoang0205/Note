package com.muamuathu.app.presentation.ui.note

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.muamuathu.app.R
import com.muamuathu.app.presentation.common.CheckAndRequestPermission
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.nav.initEventHandler

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://journal/null")

enum class MediaType {
    IMAGE,
    VIDEO
}

@Composable
fun ScreenPickFile(mediaType: MediaType) {
    val context = LocalContext.current
    val eventHandler = initEventHandler()
    Content(mediaType = mediaType, onImageUri = {
        Toast.makeText(context, "Capture Image: ${it.path}", Toast.LENGTH_LONG).show()
        eventHandler.postNavEvent(NavEvent.PopBackStack())
    })
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun Content(
    mediaType: MediaType,
    onImageUri: (Uri) -> Unit,
) {

    val accessMediaPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    LaunchedEffect(key1 = true, block = {
        accessMediaPermissionState.launchPermissionRequest()
    })

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI)
        }
    )

    @Composable
    fun OpenFileManager() {
        SideEffect {
            when (mediaType) {
                MediaType.IMAGE -> launcher.launch("image/*")
                MediaType.VIDEO -> launcher.launch("video/*")
            }
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        CheckAndRequestPermission(permissionState = accessMediaPermissionState,
            rationalePermissionMessage = R.string.msg_storage_rationale_permission,
            permissionMessage = R.string.msg_storage_permission) {
            OpenFileManager()
        }
    } else {
        OpenFileManager()
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(MediaType.IMAGE, {})
}



