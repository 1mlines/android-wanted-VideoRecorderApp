package com.preonboarding.videorecorder.data.usecase

import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.repository.VideoRepository
import com.preonboarding.videorecorder.domain.usecase.GetVideoListUseCase
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
) : GetVideoListUseCase {
    override suspend fun getVideoDataList(): List<Video> {
        return videoRepository.getVideoList()
    }
}