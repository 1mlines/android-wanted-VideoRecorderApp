package com.preonboarding.videorecorder.domain.repository

import com.preonboarding.videorecorder.domain.model.Video

interface VideoRepository {
    suspend fun saveVideo(video: Video)
    suspend fun deleteVideo(video: Video)
    suspend fun getVideoList(): List<Video>
}