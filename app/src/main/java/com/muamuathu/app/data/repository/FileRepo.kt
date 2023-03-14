package com.solid.journal.data.repository

import android.graphics.Bitmap
import com.solid.journal.data.model.note.FileInfo
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileRepo {
    fun loadMediaFile(isImage: Boolean): Flow<List<FileInfo>>
    fun saveImageDrawSketch(bitmap: Bitmap): Flow<String>
}