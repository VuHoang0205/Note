package com.muamuathu.ui.camerax.video

import androidx.camera.core.TorchState
import androidx.camera.video.Quality
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import java.io.File

@Composable
fun VideoCaptureView(
    modifier: Modifier,
    cameraLens: Int?,
    @TorchState.State torchState: Int,
    quality: Quality,
    recordState: RecordState,
    outputFile: File,
    videoCaptureListener: VideoCaptureManager.Listener
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val captureManager = remember {
        VideoCaptureManager.Builder(context, outputFile, quality)
            .registerLifecycleOwner(lifecycleOwner)
            .create()
            .apply {
                this.listener = videoCaptureListener
            }
    }

    LaunchedEffect(recordState) {
        when (recordState) {
            Started -> captureManager.startRecording()
            Paused -> captureManager.pauseRecording()
            Resumed -> captureManager.resumeRecording()
            Stopped -> captureManager.stopRecording()
            else -> {

            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            captureManager.stopRecording()
        }
    }

    CompositionLocalProvider(LocalVideoCaptureManager provides captureManager) {
        cameraLens?.let {
            CameraPreview(
                modifier = modifier,
                lens = cameraLens,
                torchState = torchState
            )
        }
    }
}