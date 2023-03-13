package com.muamuathu.ui.camerax.video

import android.util.Size
import androidx.camera.core.TorchState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun CameraPreview(modifier: Modifier, lens: Int, @TorchState.State torchState: Int) {
    val captureManager = LocalVideoCaptureManager.current
    BoxWithConstraints(modifier = modifier) {
        AndroidView(
            factory = {
                captureManager.showPreview(
                    PreviewState(
                        cameraLens = lens,
                        torchState = torchState,
                        size = Size(this.minWidth.value.toInt(), this.maxHeight.value.toInt())
                    )
                )
            },
            modifier = Modifier.fillMaxSize(),
            update = {
                captureManager.updatePreview(
                    PreviewState(cameraLens = lens, torchState = torchState),
                    it
                )
            }
        )
    }
}