package com.example.data.source.remote

import androidx.paging.PagingData
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface VideoRemoteDataSource {
    suspend fun uploadVideoStorage(video: Video): FirebaseResponse<Nothing>

    suspend fun uploadVideoFirestore(video: Video): FirebaseResponse<Nothing>

    fun getVideoList(): Flow<PagingData<Video>>

    suspend fun deleteVideoFirestore(video: Video): FirebaseResponse<Nothing>

    suspend fun deleteVideoStorage(video: Video): FirebaseResponse<Nothing>
}