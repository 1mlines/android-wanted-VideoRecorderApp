package com.preonboarding.videorecorder.presentation.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.preonboarding.videorecorder.databinding.ItemVideoBinding
import com.preonboarding.videorecorder.domain.model.Video
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class MainAdapter(
    private val itemClick: (Video) -> Unit,
    private val deleteClick: (Video) -> Unit
) : ListAdapter<Video, MainAdapter.MainViewHolder>(diffUtil) {

    lateinit var factory : DataSource.Factory
    lateinit var mediaSource : ProgressiveMediaSource
    lateinit var exoPlayer: ExoPlayer

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
                mediaSource = ProgressiveMediaSource.Factory(factory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(video.uri)))

                exoPlayer.apply {
                    setMediaSource(mediaSource)
                    prepare()
                    play()
                }

                binding.itemNewsPlayerView.apply {
                    player = exoPlayer
                    useController = false
                }
                true
            }

//            binding.cvVideoItem.setOnLongClickListener {
//                mediaSource = ProgressiveMediaSource.Factory(factory)
//                    .createMediaSource(MediaItem.fromUri(Uri.parse(video.uri)))
//
//                exoPlayer.apply {
//                    setMediaSource(mediaSource)
//                    prepare()
//                    play()
//                }
//
//                binding.itemNewsPlayerView.apply {
//                    player = exoPlayer
//                    useController = false
//                }
//                true
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.apply {
            factory = DefaultDataSource.Factory(parent.context)
            exoPlayer = ExoPlayer.Builder(binding.root.context).build()
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