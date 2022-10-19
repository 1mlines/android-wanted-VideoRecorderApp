package com.preonboarding.videorecorder.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Video
import com.preonboarding.videorecorder.databinding.RvVideoBinding

//todo PagingAdapter
class VideoListAdapter : ListAdapter<Video, VideoListAdapter.VideoViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder =
        VideoViewHolder(RvVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun removeItem(position: Int) {
        currentList.toMutableList().apply {
            removeAt(position)
            submitList(this)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                oldItem.hashCode() == newItem.hashCode()

        }
    }

    class VideoViewHolder(private val binding: RvVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            binding.video = video
        }
    }


}