package com.preonboarding.videorecorder.domain.usecase

import com.preonboarding.videorecorder.domain.model.Video

interface DeleteVideoUseCase {
    suspend fun deleteVideoData(video: Video)
}