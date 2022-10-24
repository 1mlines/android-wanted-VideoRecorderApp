package com.preonboarding.videorecorder.data.di

import com.preonboarding.videorecorder.data.source.FirebaseDataSource
import com.preonboarding.videorecorder.data.source.FirebaseDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsDataSource(datasource: FirebaseDataSourceImpl): FirebaseDataSource
}