package com.preonboarding.videorecorder.data.source

import com.preonboarding.videorecorder.domain.model.Video

class FirebaseDataSourceImpl : FirebaseDataSource {
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