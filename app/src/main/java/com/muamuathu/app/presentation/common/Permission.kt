@file:OptIn(ExperimentalPermissionsApi::class)

package com.muamuathu.app.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.muamuathu.app.R

@Composable
fun CheckAndRequestPermission(
    modifier: Modifier = Modifier.fillMaxSize(),
    permissionState: PermissionState,
    rationalePermissionMessage: Int,
    permissionMessage: Int,
    onGrantedPermission: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        if (permissionState.hasPermission) {
            onGrantedPermission()
        } else if (permissionState.permissionRequested) {
            Column(modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                val textToShow = if (permissionState.shouldShowRationale) {
                    stringResource(id = rationalePermissionMessage)
                } else {
                    stringResource(id = permissionMessage)
                }
                Text(textToShow)
                Button(onClick = { permissionState.launchPermissionRequest() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(
                        R.color.royal_blue))) {
                    Text(stringResource(R.string.request_permission),
                        color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CheckAndRequestMultiPermission(
    modifier: Modifier = Modifier.fillMaxSize(),
    multiplePermissionsState: MultiplePermissionsState,
    rationalePermissionMessages: List<Int>,
    permissionMessages: List<Int>,
    onGrantedPermission: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        if (multiplePermissionsState.allPermissionsGranted) {
            onGrantedPermission()
        } else if (multiplePermissionsState.permissionRequested) {
            for (i in 0..multiplePermissionsState.permissions.size) {
                val permissionState = multiplePermissionsState.permissions[i]
                if (permissionState.hasPermission) {
                    Column(modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        val textToShow = if (multiplePermissionsState.shouldShowRationale) {
                            stringResource(id = rationalePermissionMessages[i])
                        } else {
                            stringResource(id = permissionMessages[i])
                        }
                        Text(textToShow)
                        Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(
                                R.color.royal_blue))) {
                            Text(stringResource(R.string.request_permission),
                                color = Color.White)
                        }
                    }
                    break
                }
            }
        }
    }
}

fun CheckAndAskPermission(
    permissionState: PermissionState,
    onGrantedPermission: () -> Unit,
    onDeniedPermission: () -> Unit = {},
    onNoRequestedPermission: () -> Unit = {},
    onDeniedRationalePermission: () -> Unit = {},
) {
    if (permissionState.hasPermission) {
        onGrantedPermission()
    } else if (permissionState.permissionRequested) {
        if (permissionState.shouldShowRationale) {
            onDeniedRationalePermission()
        } else {
            onDeniedPermission()
        }
    } else {
        onNoRequestedPermission()
    }
}
