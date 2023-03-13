package com.muamuathu.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideCoroutineContext(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}