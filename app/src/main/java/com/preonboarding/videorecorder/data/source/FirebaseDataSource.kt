package com.preonboarding.videorecorder.data.source

import com.preonboarding.videorecorder.domain.model.Video

interface FirebaseDataSource {
    suspend fun saveVideo(video: Video)
    suspend fun deleteVideo(video: Video)
    suspend fun getVideoList(): List<Video>
}