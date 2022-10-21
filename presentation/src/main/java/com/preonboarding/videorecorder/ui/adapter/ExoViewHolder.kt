package com.preonboarding.videorecorder.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Video
import com.preonboarding.videorecorder.databinding.RvVideoBinding
import com.preonboarding.videorecorder.util.DateUtil

class ExoViewHolder(val binding: RvVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(video: Video) {
        binding.video = video
        binding.date.text = DateUtil.convert(video.publishedAt)
    }
}