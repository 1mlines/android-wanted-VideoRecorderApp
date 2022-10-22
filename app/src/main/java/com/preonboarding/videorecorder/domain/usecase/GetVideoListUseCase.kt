package com.preonboarding.videorecorder.domain.usecase

import com.preonboarding.videorecorder.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface GetVideoListUseCase {
    suspend operator fun invoke(): Flow<List<Video>>
}