package com.example.data.util

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.FirebaseState
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await
import timber.log.Timber

suspend fun <T> safeSetCall(callFunction: () -> Task<T>): FirebaseResponse<Nothing> {
    return try {
        callFunction.invoke().await()
        FirebaseResponse(FirebaseState.SUCCESS)
    } catch (e: Exception) {
        Timber.e("safeSetCall: 실패 $e")
        FirebaseResponse(FirebaseState.FAILURE)
    }
}