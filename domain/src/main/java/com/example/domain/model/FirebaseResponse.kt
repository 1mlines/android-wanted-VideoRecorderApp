package com.example.domain.model

data class FirebaseResponse<T>(
    val state : FirebaseState,
    val result : T?
)

enum class FirebaseState{
    SUCCESS,
    FAILURE,
}