package com.preonboarding.videorecorder.domain.repository

import com.preonboarding.videorecorder.data.entity.FirebaseStorageState
import com.preonboarding.videorecorder.data.entity.RemoteVideo
import com.preonboarding.videorecorder.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    suspend fun getVideoList(): Flow<List<RemoteVideo>>

    suspend fun getVideo(): Flow<FirebaseStorageState<List<Video>>>

    suspend fun uploadVideo(video: Video)

    suspend fun deleteVideo(video: Video)
}