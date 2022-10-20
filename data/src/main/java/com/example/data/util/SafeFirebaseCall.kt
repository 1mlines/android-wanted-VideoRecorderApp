package com.example.data.util

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.FirebaseState
import com.example.domain.model.Video
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import timber.log.Timber

suspend fun safeGetCall(callFunction: () -> Task<QuerySnapshot>) : FirebaseResponse<List<Video>> {
    var state = FirebaseState.FAILURE
    val result = arrayListOf<Video>()

    callFunction.invoke()
        .addOnSuccessListener { querySnapShot ->
            result.clear()
            Timber.d("safeGetCall: 성공 ${querySnapShot}")
            for (item in querySnapShot.documents) {
                item.toObject(Video::class.java).let { it
                    result.add(it?:Video())
                }
            }
            state = FirebaseState.SUCCESS
        }
        .addOnFailureListener {
            Timber.e("safeGetCall: 실패 ${it}")
            state = FirebaseState.FAILURE
        }.await()

    return FirebaseResponse(state = state, result = result)
}

suspend fun <T> safeSetCall( callFunction: () -> Task<T>) : FirebaseResponse<Nothing> {
    var state = FirebaseState.FAILURE
    callFunction.invoke()
        .addOnSuccessListener {
            Timber.d("safeSetCall: 성공 ${it}")
            state = FirebaseState.SUCCESS
        }
        .addOnFailureListener {
            state = FirebaseState.FAILURE
            Timber.e("safeSetCall: 실패 ${it}")
        }.await()

    return FirebaseResponse(state = state)
}