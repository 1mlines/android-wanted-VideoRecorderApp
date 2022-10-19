package com.example.data.usecase

import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.InsertVideoDataUseCase
import javax.inject.Inject

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:45 PM
 */
class InsertVideoDataUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
) : InsertVideoDataUseCase {
    override suspend fun invoke(video: Video) {
        return videoRepository.insertVideo(video = video)
    }
}
