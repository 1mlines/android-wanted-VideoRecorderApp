package com.preonboarding.videorecorder.data.usecase

import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.repository.VideoRepository
import com.preonboarding.videorecorder.domain.usecase.SaveVideoUseCase
import javax.inject.Inject

class SaveVideoUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
): SaveVideoUseCase{
    override suspend fun invoke(video: Video) {
        videoRepository.saveVideo(video)
    }
}