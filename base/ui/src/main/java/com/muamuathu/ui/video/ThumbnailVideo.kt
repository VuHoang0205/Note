package com.muamuathu.ui.video

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File

@Composable
fun ThumbnailVideo(modifier: Modifier, file: File) {
    AndroidView(
        modifier = modifier,
        factory = {
            ImageView(it).also {
                it.scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(it)
                    .load(file)
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(it)
            }
        }
    )
}