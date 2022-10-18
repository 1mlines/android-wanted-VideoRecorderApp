package com.preonboarding.videorecorder.domain.usecase

import com.preonboarding.videorecorder.domain.model.Video

interface SaveVideoUseCase {
    suspend operator fun invoke(video: Video)
}