package com.example.data.source.remote

import android.net.Uri
import android.util.Log
import com.example.data.util.safeSetFireStoreCall
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class VideoRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
) : VideoRemoteDataSource {
    override suspend fun uploadVideoStorage(uri: Uri) {
        val publishedAt = System.currentTimeMillis()
        val fileName = "${publishedAt}.mp4"
//        firebaseStorage.reference.child(fileName)
//            .putFile(uri)
//            .addOnSuccessListener {
//                uploadVideoFirestore(fileName, publishedAt)
//            }
//            .addOnFailureListener {
//
//            }
    }

    override suspend fun uploadVideoFirestore(fileName: String, publishedAt: Long) {
        firebaseStorage.reference.child(fileName).downloadUrl.addOnSuccessListener { uri ->
            firestore.collection("videos")
                .add(Video(name = fileName, publishedAt = publishedAt, uri = uri.toString()))
                .addOnSuccessListener {
                    Log.d("abc", "onCreate: success")
                }
        }
    }

    override suspend fun getVideoList(): FirebaseResponse<List<Video>> {
        return safeSetFireStoreCall {
            firestore.collection("videos")
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .get()
        }
    }

    override fun deleteVideo() {

    }

}