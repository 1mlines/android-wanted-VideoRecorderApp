package com.preonboarding.videorecorder.domain.model

data class Video(
    val id: Int,
    val uri: String,
    val date: String
) {
    companion object {
        val EMPTY = Video(
            id = 0,
            uri = "",
            date = ""
        )
    }
}