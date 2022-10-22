package com.example.di

import com.example.data.usecase.DeleteVideoFirestoreUseCaseImpl
import com.example.data.usecase.DeleteVideoStorageUseCaseImpl
import com.example.data.usecase.GetVideoListUseCaseImpl
import com.example.data.usecase.UploadVideoUseCaseImpl
import com.example.domain.usecase.DeleteVideoFirestoreUseCase
import com.example.domain.usecase.DeleteVideoStorageUseCase
import com.example.domain.usecase.GetVideoListUseCase
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
    abstract fun bindDeleteVideoStorageUseCase(deleteVideoStorageUseCaseImpl: DeleteVideoStorageUseCaseImpl): DeleteVideoStorageUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteVideoFirestoreUseCase(deleteVideoFirestoreUseCaseImpl: DeleteVideoFirestoreUseCaseImpl): DeleteVideoFirestoreUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetVideoListUseCase(getVideoListUseCaseImpl: GetVideoListUseCaseImpl): GetVideoListUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUploadVideoUseCase(uploadVideoUseCaseImpl: UploadVideoUseCaseImpl): UploadVideoUseCase

}
