package com.preonboarding.videorecorder.presentation.ui.playvideo

import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.util.Util
import com.preonboarding.videorecorder.databinding.ActivityVideoPlayBinding

class VideoPlayActivity : AppCompatActivity() {

    val binding by lazy { ActivityVideoPlayBinding.inflate(layoutInflater) }
    var player : ExoPlayer ? = null
    val playerView by lazy {  binding.playView }
    val url = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
    val mediaItem by lazy {  MediaItem.fromUri(url) }

    // Android 24 이하에서 player 해제용
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //


    }

    fun initPlay(){
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        binding.playControlView.player = player

        player!!.also {
            it.setMediaItem(mediaItem)
            it.playWhenReady = playWhenReady
            it.seekTo(currentWindow, playbackPosition)
            it.prepare()
        }

    }

    override fun onStart() {
        if (Util.SDK_INT >= 24) {
            initPlay()
        }
        super.onStart()
    }

    override fun onResume() {
        if ((Util.SDK_INT < 24 || player == null)) {
            initPlay()
        }
        super.onResume()
    }

    private fun releasePlayer() {
        // 플레이어 해제 전, 재생정보 저장 -> 굳이 필요 없을듯...
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }











}