package com.muamuathu.app.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.muamuathu.app.domain.model.FileInfo
import com.muamuathu.app.presentation.helper.ResultWrapper

interface FileRepo {
    suspend fun loadMediaFile(isImage: Boolean): ResultWrapper<List<FileInfo>>
    suspend fun saveImageDrawSketch(bitmap: Bitmap): ResultWrapper<String>
    suspend fun getPathFromUri(uri: Uri): ResultWrapper<String>
}