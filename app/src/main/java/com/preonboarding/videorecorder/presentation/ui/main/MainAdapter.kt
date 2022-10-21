package com.preonboarding.videorecorder.presentation.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.preonboarding.videorecorder.databinding.ItemVideoBinding
import com.preonboarding.videorecorder.domain.model.Video

class MainAdapter(
    private val itemClick: (Video) -> Unit,
    private val deleteClick: (Video) -> Unit,
    private val itemLongClick: (String) -> ExoPlayer
) : ListAdapter<Video, MainAdapter.MainViewHolder>(diffUtil) {

    inner class MainViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            binding.video = video

            binding.itemNewsPlayerView.setOnClickListener {
                itemClick(video)
            }
            binding.btnDelete.setOnClickListener {
                deleteClick(video)
            }

            binding.itemNewsPlayerView.setOnLongClickListener {
                binding.itemNewsPlayerView.useController = false
                binding.itemNewsPlayerView.player = itemLongClick(video.uri)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.apply {
        }
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}