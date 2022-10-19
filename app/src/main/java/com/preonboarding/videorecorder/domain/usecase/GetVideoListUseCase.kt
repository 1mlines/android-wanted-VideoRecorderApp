package com.preonboarding.videorecorder.domain.usecase

import com.preonboarding.videorecorder.domain.model.Video

interface GetVideoListUseCase {
    suspend operator fun invoke(): List<Video>
}