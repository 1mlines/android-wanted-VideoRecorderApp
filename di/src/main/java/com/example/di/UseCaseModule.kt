package com.example.di

import com.example.data.usecase.DeleteVideoDataUseCaseImpl
import com.example.data.usecase.GetVideoListUseCaseImpl
import com.example.data.usecase.InsertVideoDataUseCaseImpl
import com.example.data.usecase.UploadVideoUseCaseImpl
import com.example.domain.usecase.DeleteVideoDataUseCase
import com.example.domain.usecase.GetVideoListUseCase
import com.example.domain.usecase.InsertVideoDataUseCase
import com.example.domain.usecase.UploadVideoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:48 PM
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindInsertVideoUseCase(insertVideoDataUseCaseImpl: InsertVideoDataUseCaseImpl): InsertVideoDataUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteVideoUseCase(deleteVideoDataUseCaseImpl: DeleteVideoDataUseCaseImpl): DeleteVideoDataUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetVideoListUseCase(getVideoListUseCaseImpl: GetVideoListUseCaseImpl): GetVideoListUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUploadVideoUseCase(uploadVideoUseCaseImpl: UploadVideoUseCaseImpl): UploadVideoUseCase

}
