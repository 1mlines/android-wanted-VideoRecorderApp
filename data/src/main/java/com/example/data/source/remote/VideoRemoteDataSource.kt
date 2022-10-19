package com.example.data.source.remote

import android.net.Uri
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video

interface VideoRemoteDataSource {
    suspend fun uploadVideoStorage(uri: Uri)

    suspend fun uploadVideoFirestore(fileName: String, publishedAt: Long)

    suspend fun getVideoList(): FirebaseResponse<List<Video>>

    fun deleteVideo()
}