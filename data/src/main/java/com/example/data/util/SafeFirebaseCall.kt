package com.example.data.util

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.FirebaseState
import com.example.domain.model.Video
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

suspend fun safeSetFireStoreCall(callFunction: () -> Task<QuerySnapshot>) : FirebaseResponse<List<Video>> {
    var state = FirebaseState.FAILURE
    val result = arrayListOf<Video>()

    callFunction.invoke()
        .addOnSuccessListener { querySnapShot ->
            result.clear()
            for (item in querySnapShot.documents) {
                item.toObject(Video::class.java).let { it
                    result.add(it?:Video())
                }
            }
            state = FirebaseState.SUCCESS
        }
        .addOnFailureListener {
            state = FirebaseState.FAILURE
        }.await()

    return FirebaseResponse(state = state, result = result)
}