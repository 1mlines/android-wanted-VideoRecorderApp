package com.preonboarding.videorecorder.domain.usecase

import com.preonboarding.videorecorder.domain.model.Video

interface GetVideoListUseCase {
    suspend fun getVideoDataList(): List<Video>
}