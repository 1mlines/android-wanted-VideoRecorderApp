package com.example.data.source.remote

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video

interface VideoRemoteDataSource {
    suspend fun uploadVideoStorage(video: Video): FirebaseResponse<Nothing>

    suspend fun uploadVideoFirestore(video: Video): FirebaseResponse<Nothing>

    suspend fun getVideoList(): FirebaseResponse<List<Video>>

    suspend fun deleteVideoFirestore(video: Video): FirebaseResponse<Nothing>

    suspend fun deleteVideoStorage(video: Video): FirebaseResponse<Nothing>
}