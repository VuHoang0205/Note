package com.muamuathu.app.presentation.ui.note.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.muamuathu.app.data.repository.FileRepo
import com.muamuathu.app.domain.model.FileInfo
import com.muamuathu.app.presentation.common.BaseViewModel
import com.muamuathu.app.presentation.helper.ResultWrapper
import com.muamuathu.app.presentation.helper.resultFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SelectFileViewModel @Inject constructor(private val fileRepo: FileRepo) : BaseViewModel() {

    private val allMediaStateFlow = MutableStateFlow<MutableList<FileInfo>>(mutableListOf())
    val pathDrawSketchStateFlow = mutableStateOf("")

    fun loadMediaFile(isImage: Boolean) = resultFlow {
        fileRepo.loadMediaFile(isImage).apply {
            if (this is ResultWrapper.Success) {
                allMediaStateFlow.value = value.toMutableList()
            }
        }
    }

    fun getOutputDirectory(context: Context): File {
        val mediaDir = context.externalCacheDirs.firstOrNull()?.let {
            File(it, context.applicationInfo.name).apply { mkdirs() }
        }
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    fun saveImageDrawSketch(bitmap: Bitmap) = resultFlow {
        fileRepo.saveImageDrawSketch(bitmap)
    }

    fun getRealPathFromUri(uri: Uri) = resultFlow {
        fileRepo.getPathFromUri(uri)
    }

    fun bindMediaList() = allMediaStateFlow.asSharedFlow()
}