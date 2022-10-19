package com.example.di

import com.example.data.source.remote.VideoRemoteDataSource
import com.example.data.source.remote.VideoRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    @ViewModelScoped
    abstract fun provideVideoDataSource(
        videoRemoteDataSourceImpl: VideoRemoteDataSourceImpl
    ) : VideoRemoteDataSource
}