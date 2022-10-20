package com.example.data.usecase

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.DeleteVideoStorageUseCase
import javax.inject.Inject

class DeleteVideoStorageUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository,
) : DeleteVideoStorageUseCase {
    override suspend fun invoke(video: Video): FirebaseResponse<Nothing> =
        videoRepository.deleteVideoStorage(video)
}