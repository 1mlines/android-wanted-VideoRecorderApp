package com.example.di

import com.example.data.repository.VideoRepositoryImpl
import com.example.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:39 PM
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun provideVideoRepository(repositoryImpl: VideoRepositoryImpl): VideoRepository
}
