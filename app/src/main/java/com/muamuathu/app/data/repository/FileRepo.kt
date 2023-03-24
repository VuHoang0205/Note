package com.muamuathu.app.data.repository

import android.graphics.Bitmap
import com.muamuathu.app.domain.model.FileInfo
import kotlinx.coroutines.flow.Flow

interface FileRepo {
    suspend fun loadMediaFile(isImage: Boolean): Flow<List<FileInfo>>
    suspend fun saveImageDrawSketch(bitmap: Bitmap): Flow<String>
}