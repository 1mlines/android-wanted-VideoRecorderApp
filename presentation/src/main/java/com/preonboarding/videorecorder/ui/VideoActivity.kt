package com.preonboarding.videorecorder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.domain.model.Video
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.preonboarding.videorecorder.R
import com.preonboarding.videorecorder.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {

    private val binding: ActivityVideoBinding by lazy {
        ActivityVideoBinding.inflate(layoutInflater)
    }

    private var player: ExoPlayer? = null

    private var uri: String? = null
    private var currentPosition: Long = 0L
    private var playerListener: Player.Listener = getPlayerListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.getBundleExtra(getString(R.string.mediaBundle))?.also {
            uri = it.getString(getString(R.string.mediaUri)) ?: ""
            currentPosition = it.getLong(getString(R.string.mediaCurrentPosition)) ?: 0L
        }

    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()

        initialize()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun hideSystemUi(){
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        }
    }

    private fun initialize() {
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                binding.playerControlView.player = exoPlayer

                uri?.let {
                    exoPlayer.setMediaItem(MediaItem.fromUri(it))
                }
                exoPlayer.addListener(playerListener)
                exoPlayer.playWhenReady = true
                exoPlayer.seekTo(currentPosition)
                exoPlayer.prepare()
            }
    }

    private fun getPlayerListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {}
                ExoPlayer.STATE_BUFFERING -> {
                    binding.progressBar.isVisible = true
                }
                ExoPlayer.STATE_READY -> {
                    binding.progressBar.isVisible = false
                }
                ExoPlayer.STATE_ENDED -> {
                    binding.progressBar.isVisible = false
                }
                else -> {}
            }
        }
    }

    private fun releasePlayer() {
        player?.run {
            removeListener(playerListener)
            release()
        }
        player = null
    }
}