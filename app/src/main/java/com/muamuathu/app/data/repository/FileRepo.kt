package com.solid.journal.data.repository

import android.graphics.Bitmap
import com.muamuathu.app.data.model.note.FileInfo
import kotlinx.coroutines.flow.Flow

interface FileRepo {
    fun loadMediaFile(isImage: Boolean): Flow<List<FileInfo>>
    fun saveImageDrawSketch(bitmap: Bitmap): Flow<String>
}