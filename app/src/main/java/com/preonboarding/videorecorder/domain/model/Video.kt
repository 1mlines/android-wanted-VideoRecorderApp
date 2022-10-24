package com.preonboarding.videorecorder.domain.model

data class Video(
    val uri: String,
    val date: String
) {
    companion object {
        val EMPTY = Video(
            uri = "",
            date = ""
        )
    }
}
