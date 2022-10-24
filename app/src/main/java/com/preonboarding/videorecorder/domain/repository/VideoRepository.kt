package com.preonboarding.videorecorder.domain.repository

import com.preonboarding.videorecorder.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRepository {
    suspend fun saveVideo(video: Video)
    suspend fun deleteVideo(video: Video)
    suspend fun getVideoList(): Flow<List<Video>>
}
