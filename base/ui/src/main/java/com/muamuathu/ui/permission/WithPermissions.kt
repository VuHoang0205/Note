package com.muamuathu.ui.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.muamuathu.ui.dialog.JcDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WithPermissions(
    permission: List<String>,
    explainTitle: String,
    explainMessage: String,
    explainAllowButton: String,
    notAvailableTitle: String,
    notAvailableMessage: String,
    notAvailableCtaText: String,
    onPermissionDenied: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permissionState = rememberMultiplePermissionsState(permission)
    var notGrantedPermissions by remember {
        mutableStateOf(false)
    }

    var notAvailableDialog by remember {
        mutableStateOf(false)
    }

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            if (permissionState.shouldShowRationale) {
                LaunchedEffect(Unit) {
                    notGrantedPermissions = true
                }
            } else {
                LaunchedEffect(Unit) {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        },
        permissionsNotAvailableContent = {
            LaunchedEffect(Unit) {
                notAvailableDialog = true
            }
        },
        content = content
    )

    if (notGrantedPermissions) {
        JcDialog(
            title = explainTitle,
            message = explainMessage,
            positiveButtonText = explainAllowButton,
            onPositiveClicked = {
                notGrantedPermissions = false
                permissionState.launchMultiplePermissionRequest()
            },
            onDismissRequest = {
                notGrantedPermissions = false
            },
            negativeButtonText = "",
            onNegativeClicked = {}
        )
    }

    if (notAvailableDialog) {
        JcDialog(
            title = notAvailableTitle,
            message = notAvailableMessage,
            positiveButtonText = notAvailableCtaText,
            onPositiveClicked = {
                notAvailableDialog = false
                context.openAppSystemSettings()
            },
            onDismissRequest = {
                notAvailableDialog = false
                onPermissionDenied()
            },
            negativeButtonText = "",
            onNegativeClicked = {}
        )
    }
}

private fun Context.openAppSystemSettings() {
    startActivity(
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
    )
}