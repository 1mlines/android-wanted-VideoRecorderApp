package com.example.data.source.remote

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.domain.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class VideoPagingSource(
    private val firestore: FirebaseFirestore,
) : PagingSource<QuerySnapshot, Video>() {
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Video> {
        return try {
            val query = firestore.collection("videos")
                .orderBy("publishedAt", Query.Direction.DESCENDING)
                .limit(PAGE_SIZE.toLong())
            val currentPage = params.key ?: query.get().await()
            val lastVisibleVideo = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleVideo).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Video::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Video>): QuerySnapshot? {
        return null
    }
}