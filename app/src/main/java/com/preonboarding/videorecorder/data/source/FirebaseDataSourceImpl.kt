package com.preonboarding.videorecorder.data.source

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.preonboarding.videorecorder.domain.model.Video
import timber.log.Timber
import javax.inject.Inject

class FirebaseDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : FirebaseDataSource {
    override suspend fun saveVideo(video: Video) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVideo(video: Video) {
        TODO("Not yet implemented")
    }

    override suspend fun getVideoList(): List<Video> {
        TODO("Not yet implemented")
    }
}