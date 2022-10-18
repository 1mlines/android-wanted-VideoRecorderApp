package com.preonboarding.videorecorder.data.usecase

import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.repository.VideoRepository
import com.preonboarding.videorecorder.domain.usecase.DeleteVideoUseCase
import javax.inject.Inject

class DeleteVideoUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
): DeleteVideoUseCase{
    override suspend fun deleteVideoData(video: Video) {
        videoRepository.deleteVideo(video)
    }
}