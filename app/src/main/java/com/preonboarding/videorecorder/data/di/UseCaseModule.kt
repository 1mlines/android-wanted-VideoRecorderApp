package com.preonboarding.videorecorder.data.di

import com.preonboarding.videorecorder.data.usecase.DeleteVideoUseCaseImpl
import com.preonboarding.videorecorder.data.usecase.GetVideoListUseCaseImpl
import com.preonboarding.videorecorder.data.usecase.SaveVideoUseCaseImpl
import com.preonboarding.videorecorder.domain.usecase.DeleteVideoUseCase
import com.preonboarding.videorecorder.domain.usecase.GetVideoListUseCase
import com.preonboarding.videorecorder.domain.usecase.SaveVideoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    @ViewModelScoped
    abstract fun bindsSaveVideoUseCase(useCase: SaveVideoUseCaseImpl) : SaveVideoUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindsDeleteVideoUseCase(useCase: DeleteVideoUseCaseImpl) : DeleteVideoUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindsGetVideoListUseCase(useCase: GetVideoListUseCaseImpl) : GetVideoListUseCase
}