package com.example.data.usecase

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.GetVideoListUseCase
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
): GetVideoListUseCase {
    override suspend fun invoke(): FirebaseResponse<List<Video>> = videoRepository.getVideoList()

}