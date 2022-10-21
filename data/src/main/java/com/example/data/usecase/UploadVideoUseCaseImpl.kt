package com.example.data.usecase

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.UploadVideoUseCase
import javax.inject.Inject

class UploadVideoUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository,
) : UploadVideoUseCase {
    override suspend fun invoke(video: Video): FirebaseResponse<Nothing> =
        videoRepository.uploadVideo(video)
}