package com.preonboarding.videorecorder.data.source

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.preonboarding.videorecorder.domain.model.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class FirebaseDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseRTDB: FirebaseDatabase
) : FirebaseDataSource {
    override suspend fun saveVideo(video: Video) {
        val fileRef = firebaseStorage.reference.child(video.date)

        fileRef.putFile(
            Uri.parse(video.uri)
        ).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                firebaseRTDB.reference
                    .child(video.date)
                    .setValue(
                        Video(
                            uri.toString(),
                            video.date
                        )
                    )
                    .addOnFailureListener {
                        throw it
                    }
            }
        }.addOnFailureListener {
            Timber.e("Upload Fail", it.message.toString())
            throw it
        }
    }

    override suspend fun deleteVideo(video: Video) {
        firebaseRTDB.reference
            .child(video.date)
            .removeValue()
            .addOnFailureListener {
                throw it
            }
    }

    override suspend fun getVideoList() =
        callbackFlow {
            firebaseRTDB.reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    trySend(snapshot.children.map {
                        Video(
                            it.child("uri").value.toString(),
                            it.child("date").value.toString()
                        )
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    cancel()
                }
            })
            awaitClose()
        }.flowOn(Dispatchers.IO)
}
