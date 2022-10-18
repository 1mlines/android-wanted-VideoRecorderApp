package com.example.di

import com.example.data.usecase.DeleteVideoDataUseCaseImpl
import com.example.data.usecase.InsertVideoDataUseCaseImpl
import com.example.domain.usecase.DeleteVideoDataUseCase
import com.example.domain.usecase.InsertVideoDataUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:48 PM
 */

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideInsertVideoUseCase(insertVideoDataUseCaseImpl: InsertVideoDataUseCaseImpl): InsertVideoDataUseCase

    @Binds
    abstract fun provideDeleteVideoUseCase(deleteVideoDataUseCaseImpl: DeleteVideoDataUseCaseImpl): DeleteVideoDataUseCase
}
