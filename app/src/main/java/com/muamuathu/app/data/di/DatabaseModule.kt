package com.solid.journal.data.di

import android.content.Context
import androidx.room.Room
import com.muamuathu.app.data.JournalDatabase
import com.solid.journal.data.repository.FileRepo
import com.solid.journal.data.repository.FileRepoImpl
import com.solid.journal.data.repository.JournalRepo
import com.solid.journal.data.repository.JournalRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    companion object {
        const val DATABASE_NAME = "journal"
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): JournalDatabase {
        return Room.databaseBuilder(context, JournalDatabase::class.java, DATABASE_NAME).build()
    }

    @Provides
    @Singleton
    fun provideRepository(database: JournalDatabase): JournalRepo = JournalRepoImpl(database)

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): FileRepo =
        FileRepoImpl(context)
}