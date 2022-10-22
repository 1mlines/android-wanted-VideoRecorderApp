package com.preonboarding.videorecorder.data.usecase

import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.repository.VideoRepository
import com.preonboarding.videorecorder.domain.usecase.GetVideoListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
) : GetVideoListUseCase {
    override suspend fun invoke(): Flow<List<Video>> {
        return videoRepository.getVideoList()
    }
}