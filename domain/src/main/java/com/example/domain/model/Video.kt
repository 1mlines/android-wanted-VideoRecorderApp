package com.example.domain.model

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:32 PM
 */
data class Video(
    val name: String = "",
    val publishedAt: Long = 0L,
    val uri: String = ""
) {
//    companion object {
//        val EMPTY = Video(
//            id = 0,
//            uri = "",
//            date = ""
//        )
//    }
}
