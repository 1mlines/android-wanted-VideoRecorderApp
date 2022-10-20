package com.example.data.source.remote

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.constant.COLLECTION_NAME
import com.example.data.constant.PAGE_SIZE
import com.example.data.util.safeSetCall
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class VideoRemoteDataSourceImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
) : VideoRemoteDataSource {
    override suspend fun uploadVideoStorage(video: Video): FirebaseResponse<Nothing> {
        return safeSetCall {
            firebaseStorage.reference.child(video.name).putFile(Uri.parse(video.uri))
        }
    }

    override suspend fun uploadVideoFirestore(video: Video): FirebaseResponse<Nothing> {
        return safeSetCall {
            firebaseStorage.reference.child(video.name).downloadUrl.addOnSuccessListener {
                firestore.collection(COLLECTION_NAME).document(video.name).set(video.copy(uri = it.toString()))
            }
        }
    }

    override fun getVideoList(): Flow<PagingData<Video>> {
        VideoPagingSource(firestore)
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { VideoPagingSource(firestore) }
        ).flow
    }

    override suspend fun deleteVideoFirestore(video: Video): FirebaseResponse<Nothing> {
        return safeSetCall {
            firestore.collection(COLLECTION_NAME).document(video.name)
                .delete()
        }
    }

    override suspend fun deleteVideoStorage(video: Video): FirebaseResponse<Nothing> {
        return safeSetCall {
            firebaseStorage.reference.child(video.name).delete()
        }
    }

}