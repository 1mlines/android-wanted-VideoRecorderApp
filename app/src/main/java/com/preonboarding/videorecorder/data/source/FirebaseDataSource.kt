package com.preonboarding.videorecorder.data.source

import com.preonboarding.videorecorder.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {
    suspend fun saveVideo(video: Video)
    suspend fun deleteVideo(video: Video)
    suspend fun getVideoList(): Flow<List<Video>>
}