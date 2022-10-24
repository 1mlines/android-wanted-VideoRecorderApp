package com.preonboarding.videorecorder.data.di

import com.preonboarding.videorecorder.data.repository.VideoRepositoryImpl
import com.preonboarding.videorecorder.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsRepository(repository: VideoRepositoryImpl): VideoRepository
}