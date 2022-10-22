package com.preonboarding.videorecorder.presentation.ui.playvideo

import android.graphics.Bitmap
import android.media.session.MediaSession
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
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


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val thumbnail: Bitmap =
            applicationContext.contentResolver.loadThumbnail(
                Uri.parse(url), Size(640, 480), null)

        binding.iv.setImageBitmap(thumbnail)

    }

    fun initPlay(){
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        binding.playView.player = player

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
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

     override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }











}