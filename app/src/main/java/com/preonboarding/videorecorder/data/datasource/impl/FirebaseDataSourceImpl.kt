package com.preonboarding.videorecorder.data.datasource.impl

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.preonboarding.videorecorder.data.datasource.FirebaseDataSource
import com.preonboarding.videorecorder.domain.model.Video
import javax.inject.Inject


class FirebaseDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : FirebaseDataSource {

    // Mock Video Reference
    private val firebaseRef = firebaseStorage.reference
//    private val mockDirRef = firebaseRef.child("mock")

    private val testDirRef = firebaseRef.child("test")

    override suspend fun getVideoList(): MutableList<StorageReference> {
        return Tasks.await(testDirRef.listAll()).items
    }

    override suspend fun uploadVideo(video: Video) {
        //TODO
    }

    override suspend fun deleteVideo(video: Video) {
        //TODO
    }
}