package com.example.data.usecase

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.DeleteVideoFirestoreUseCase
import javax.inject.Inject

class DeleteVideoFirestoreUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository,
) : DeleteVideoFirestoreUseCase {
    override suspend fun invoke(video: Video): FirebaseResponse<Nothing> =
        videoRepository.deleteVideoFirestore(video)
}