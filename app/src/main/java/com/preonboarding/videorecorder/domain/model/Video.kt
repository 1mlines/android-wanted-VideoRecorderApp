package com.preonboarding.videorecorder.domain.model

import java.io.Serializable

// TODO 쓰시는분이 수정
data class Video(
    val date: String = "",
    val uri: String = "",
    //val duration : String = ""
) : Serializable
