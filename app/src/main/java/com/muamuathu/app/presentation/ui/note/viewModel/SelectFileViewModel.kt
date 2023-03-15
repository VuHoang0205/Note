package com.muamuathu.app.presentation.ui.note.viewModel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.muamuathu.app.data.model.note.FileInfo
import com.muamuathu.app.presentation.common.BaseViewModel
import com.solid.journal.data.repository.FileRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SelectFileViewModel @Inject constructor(private val fileRepo: FileRepo) : BaseViewModel() {

    private val allMediaStateFlow = MutableStateFlow<MutableList<FileInfo>>(mutableListOf())
    val pathDrawSketchStateFlow = mutableStateOf("")

    fun loadMediaFile(isImage: Boolean) = viewModelScope.launch {
        fileRepo.loadMediaFile(isImage).onEach {
            allMediaStateFlow.value = it.toMutableList()
        }.collect()
    }

    fun getOutputDirectory(context: Context): File {
        val mediaDir = context.externalCacheDirs.firstOrNull()?.let {
            File(it, context.applicationInfo.name).apply { mkdirs() }
        }
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    suspend fun saveImageDrawSketch(bitmap: Bitmap) {
        fileRepo.saveImageDrawSketch(bitmap).onEach {
            pathDrawSketchStateFlow.value = it
        }.collect()
    }

    fun bindMediaList() = allMediaStateFlow.asSharedFlow()
}