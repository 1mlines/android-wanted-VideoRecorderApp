package com.example.di

import com.example.data.repository.VideoRepositoryImpl
import com.example.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:39 PM
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun provideVideoRepository(repositoryImpl: VideoRepositoryImpl): VideoRepository

}
