package com.preonboarding.videorecorder.ui.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Video

@BindingAdapter("app:items")
fun RecyclerView.setItems(itemList: List<Video>?) {
    val videoListAdapter = VideoListAdapter()
    adapter = videoListAdapter
    videoListAdapter.submitList(itemList)
}

@BindingAdapter("app:swipe")
fun RecyclerView.addSwipe(dummy: Any?) {

    ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean =
                false


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (adapter as VideoListAdapter).removeItem(viewHolder.layoutPosition)
                //todo data delete
            }
        }
    ).attachToRecyclerView(this)

}
