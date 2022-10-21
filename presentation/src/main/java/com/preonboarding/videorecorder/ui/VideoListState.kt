package com.preonboarding.videorecorder.ui

sealed class VideoListState {
//    data class Success(
//        val data: List<Video>,
//    ) : VideoListState()

    object Failure : VideoListState()

    object Loading : VideoListState()

    object Update : VideoListState()
}