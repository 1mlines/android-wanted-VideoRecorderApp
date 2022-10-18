package com.example.data.usecase

import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.DeleteVideoDataUseCase
import javax.inject.Inject

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:47 PM
 */
class DeleteVideoDataUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
) : DeleteVideoDataUseCase {
    override suspend fun invoke(id: Long) {
        return videoRepository.deleteVideo(id = id)
    }
}
