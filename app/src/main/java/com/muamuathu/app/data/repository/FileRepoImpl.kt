package com.muamuathu.app.data.repository

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.muamuathu.app.data.base.AppLog
import com.muamuathu.app.data.constants.FILE_NAME_FORMAT
import com.muamuathu.app.domain.model.DEFAULT_ID
import com.muamuathu.app.domain.model.FileInfo
import com.muamuathu.app.presentation.helper.ResultWrapper
import com.muamuathu.app.presentation.helper.safeDataBaseCall
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class FileRepoImpl @Inject constructor(@ApplicationContext private val context: Context) :
    FileRepo {

    override suspend fun loadMediaFile(isImage: Boolean) = safeDataBaseCall {
        val resolver = context.contentResolver
        val mediaList: MutableList<FileInfo> = mutableListOf()
        val projection: Array<String>?
        val uri: Uri?
        val where: String?
        val sortOrder: String?
        if (isImage) {
            projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            sortOrder = MediaStore.Images.ImageColumns.SIZE + " > 0"
            where = MediaStore.Images.ImageColumns.SIZE + " > 0"
        } else {
            projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
            )
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            where = MediaStore.Video.VideoColumns.SIZE + " > 0"
            sortOrder = MediaStore.Video.VideoColumns.SIZE + " > 0"
        }
        val cur: Cursor? = resolver.query(
            uri, projection,
            where,
            null,
            sortOrder
        )
        cur?.apply {
            if (cur.moveToFirst()) {
                val idColumn: Int
                val sizeColumn: Int
                val nameColumn: Int
                var durationColumn = 0
                if (isImage) {
                    idColumn = getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    sizeColumn = getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                    nameColumn = getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                } else {
                    idColumn = getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    sizeColumn = getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                    nameColumn = getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                    durationColumn = getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                }
                do {
                    val size: Long = getLong(sizeColumn)
                    val id: Long = getLong(idColumn)
                    val name: String = getString(nameColumn)
                    val dataUri = Uri.withAppendedPath(uri, "" + id).toString()
                    val duration = if (isImage) 0 else getLong(durationColumn)
                    val fileInfo = FileInfo(
                        id = id.toString(),
                        name = name,
                        data = dataUri,
                        size = size,
                        duration = duration
                    )
                    mediaList.add(fileInfo)
                } while (moveToNext())
            }
            cur.close()
            mediaList.apply {
                mediaList.add(0, FileInfo(id = DEFAULT_ID))
            }.toList()
        }
        return@safeDataBaseCall mediaList.toList()
    }

    override suspend fun saveImageDrawSketch(bitmap: Bitmap) = safeDataBaseCall {
        try {
            context.externalCacheDirs.firstOrNull()?.let {
                val folder = File(it, context.applicationInfo.name).apply { mkdirs() }
                if (folder.exists()) {
                    val file = File(
                        folder, SimpleDateFormat(
                            FILE_NAME_FORMAT,
                            Locale.US
                        ).format(System.currentTimeMillis()) + ".jpg"
                    )
                    if (file.createNewFile()) {
                        var fos: FileOutputStream? = null
                        try {
                            fos = FileOutputStream(file)
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            AppLog.d("error: $e")
                        } finally {
                            try {
                                fos?.close()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                        file.absolutePath
                    }
                }
            }
        } catch (e: Exception) {
            AppLog.d("error: $e")

        }
        ""
    }

    override suspend fun getRealPathFromURI(uri: Uri): ResultWrapper<String> {
        return getRealPathFromURI(uri)
    }
}