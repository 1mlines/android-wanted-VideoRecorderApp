package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface GetVideoListUseCase {
    operator fun invoke(): Flow<PagingData<Video>>
}