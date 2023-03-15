package com.muamuathu.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.muamuathu.app.data.converters.Converters
import com.muamuathu.app.data.dao.*
import com.muamuathu.app.data.entity.Tag
import com.muamuathu.app.data.entity.Task
import com.muamuathu.app.data.entity.*
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [
        Account::class,
        Folder::class,
        LinkFolderNote::class,
        LinkFolderTask::class,
        LinkTagNode::class,
        Tag::class,
        Note::class,
        NoteItem::class,
        NoteItemMetaValue::class,
        Task::class,
        SubTask::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JournalDatabase : RoomDatabase() {

    abstract fun daoFolder(): DaoFolder
    abstract fun daoLinkFolderNote(): DaoLinkFolderNote
    abstract fun daoLinkFolderTask(): DaoLinkFolderTask
    abstract fun daoLinkTagNode(): DaoLinkTagNode
    abstract fun daoNoteItem(): DaoNoteItem
    abstract fun daoNote(): DaoNote
    abstract fun daoNoteItemMetaValue(): DaoNoteItemMetaValue
    abstract fun daoSubTask(): DaoSubTask
    abstract fun daoTag(): DaoTag
    abstract fun daoTask(): DaoTask

    fun loadSelectFolders(): Flow<List<Folder>> {
        return daoFolder().getFolders()
    }

    fun loadTags(): Flow<List<Tag>> {
        return daoTag().getTags()
    }
}