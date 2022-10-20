package com.example.data.source.remote

import android.net.Uri
import com.example.data.util.safeGetCall
import com.example.data.util.safeSetCall
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
    override suspend fun uploadVideoStorage(video: Video): FirebaseResponse<Nothing> {
        val publishedAt = System.currentTimeMillis()
        val fileName = "${publishedAt}.mp4"
        return safeSetCall {
            firebaseStorage.reference.child(fileName).putFile(Uri.parse(video.uri))
        }

//        firebaseStorage.reference.child(fileName)
//            .putFile(uri)
//            .addOnSuccessListener {
//                uploadVideoFirestore(fileName, publishedAt)
//            }
//            .addOnFailureListener {
//
//            }
    }

    override suspend fun uploadVideoFirestore(video: Video): FirebaseResponse<Nothing> {
        return safeSetCall {
            firebaseStorage.reference.child(video.name).downloadUrl.addOnSuccessListener {
                firestore.collection("videos").add(video.copy(uri = it.toString()))
            }

        }
//        firebaseStorage.reference.child(fileName).downloadUrl.addOnSuccessListener { uri ->
//            firestore.collection("videos")
//                .add(Video(name = fileName, publishedAt = publishedAt, uri = uri.toString()))
//                .addOnSuccessListener {
//                    Log.d("abc", "onCreate: success")
//                }
//        }
    }

    override suspend fun getVideoList(): FirebaseResponse<List<Video>> {
        return safeGetCall {
            firestore.collection("videos").orderBy("publishedAt", Query.Direction.DESCENDING).get()
        }
    }

    override suspend fun deleteVideoFirestore(video: Video): FirebaseResponse<Nothing> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVideoStorage(video: Video): FirebaseResponse<Nothing> {
        TODO("Not yet implemented")
    }

}