package com.example.data.usecase

import androidx.paging.PagingData
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import com.example.domain.usecase.GetVideoListUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideoListUseCaseImpl @Inject constructor(
    private val videoRepository: VideoRepository
): GetVideoListUseCase {
    override fun invoke(): Flow<PagingData<Video>> = videoRepository.getVideoList()
}