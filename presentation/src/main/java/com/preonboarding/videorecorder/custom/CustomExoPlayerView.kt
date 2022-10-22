package com.preonboarding.videorecorder.custom

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Video
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.*
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.preonboarding.videorecorder.R
import com.preonboarding.videorecorder.ui.VideoActivity
import com.preonboarding.videorecorder.ui.adapter.ExoViewHolder
import com.preonboarding.videorecorder.util.DateUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

class CustomExoPlayerView : RecyclerView {

    private var exoPlayerView: StyledPlayerView? = null
    private var player: ExoPlayer? = null
    private lateinit var exoPlayerListener: Player.Listener
    private var defaultExoPlayerHeight = 0
    private var defaultScreenHeight = 0
    private var isAddView = false
    private var playPosition = -1

    private var viewHolderParent: View? = null
    private var progressBar: ProgressBar? = null
    private var playerViewContainer: FrameLayout? = null
    private var durationText: TextView? = null
    private var duration = 0L

    private val coroutineContext = Dispatchers.Main + CoroutineExceptionHandler { _, _ -> }
    private val exoCoroutineScope = CoroutineScope(coroutineContext)
    private var durationJob: Job? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        exoPlayerView = StyledPlayerView(context)
        exoPlayerView?.resizeMode = RESIZE_MODE_FIXED_WIDTH
        exoPlayerView?.useController = false

        exoPlayerListener = playerStateListener()

        player = ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                exoPlayerView?.player = exoPlayer
                exoPlayer.addListener(exoPlayerListener)
            }

        setDefaultHeight(context)

        initRecyclerViewListener()
    }

    private fun initRecyclerViewListener() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == SCROLL_STATE_IDLE) {
                    val position = if (!recyclerView.canScrollVertically(1)) {
                        playCurrentPosition(true)
                    } else {
                        playCurrentPosition(false)
                    }

                    if (position != -1) {
                        bindPlayer()
                    }
                }
            }
        })

        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent == view) {
                    resetPlayerView()
                }
            }
        })
    }

    fun playCurrentPosition(isEndOfList: Boolean): Int {
        val targetPosition: Int = if (!isEndOfList) {
            val startPosition =
                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            var endPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            if (endPosition - startPosition > 1) {
                endPosition = startPosition + 1
            }

            if (startPosition < 0 || endPosition < 0) {
                return -1
            }

            if (startPosition != endPosition) {
                val startPositionHeight = getVisiblePlayerHeight(startPosition)
                val endPositionHeight = getVisiblePlayerHeight(endPosition)

                if (startPositionHeight > endPositionHeight) startPosition else endPosition
            } else {
                startPosition
            }
        } else {
            listSize() - 1
        }

        if (targetPosition == playPosition) {
            return playPosition
        }

        playPosition = targetPosition

        if (exoPlayerView == null) {
            return -1
        }

        return playPosition
    }

    private fun bindPlayer() {
        val currentPosition =
            playPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val exoViewHolder = getExoViewHolder(currentPosition) ?: return

        playerViewContainer = exoViewHolder.binding.playerViewContainer
        progressBar = exoViewHolder.binding.progressBar
        viewHolderParent = exoViewHolder.itemView
        durationText = exoViewHolder.binding.durationText

        val video = getItem(currentPosition)

        exoViewHolder.binding.root.setOnClickListener {
            startActivity(
                context,
                Intent(context, VideoActivity::class.java).apply {
                    putExtra(
                        context.getString(R.string.mediaBundle),
                        bundleOf(
                            context.getString(R.string.mediaUri) to video.uri,
                            context.getString(R.string.mediaCurrentPosition) to player?.currentPosition
                        )
                    )
                },
                null
            )
        }

        exoPlayerListener = playerStateListener()
        initializePlayer(video)
    }

    private fun getExoViewHolder(currentPosition: Int): ExoViewHolder? {
        val child = getChildAt(currentPosition) ?: return null
        val exoViewHolder = getChildViewHolder(child) as? ExoViewHolder

        removePlayerView()

        if (exoViewHolder == null) {
            playPosition = -1
            return null
        }

        return exoViewHolder
    }

    private fun getVisiblePlayerHeight(playPosition: Int): Int {
        val positionAt =
            playPosition - (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        val childView = getChildAt(positionAt) ?: return 0

        val location = IntArray(2)
        childView.getLocationInWindow(location)

        return if (location[1] < 0) {
            location[1] + defaultExoPlayerHeight
        } else {
            defaultScreenHeight - location[1]
        }
    }

    fun initializePlayer(video: Video? = null) {
        if (player == null) {
            player = ExoPlayer.Builder(context)
                .build()
                .also { exoPlayer ->
                    exoPlayerView?.player = exoPlayer

                    video?.let {
                        exoPlayer.setMediaItem(MediaItem.fromUri(it.uri))
                    }

                    exoPlayerListener = playerStateListener()
                    exoPlayer.addListener(exoPlayerListener)

                    exoPlayer.playWhenReady = true
                    exoPlayer.seekTo(0)
                    exoPlayer.prepare()
                }

        } else {
            player?.also { exoPlayer ->
                exoPlayerView?.player = exoPlayer

                video?.let {
                    exoPlayer.setMediaItem(MediaItem.fromUri(it.uri))
                }

                exoPlayer.playWhenReady = true
                exoPlayer.seekTo(0)
                exoPlayer.prepare()
            }
        }
    }

    private fun addPlayerView() {
        playerViewContainer?.let { container ->
            container.addView(exoPlayerView)
            isAddView = true

            durationText?.isVisible = true
            container.invalidate()
            exoPlayerView?.requestFocus()
        }
    }

    private fun resetPlayerView() {
        if (isAddView) {
            removePlayerView()
            playPosition = -1

            playerViewContainer = null
            progressBar = null
            viewHolderParent = null
            durationText = null
        }
    }

    private fun removePlayerView() {
        val parent = exoPlayerView?.parent as? ViewGroup ?: return
        val idx = parent.indexOfChild(exoPlayerView)

        if (idx >= 0) {
            parent.removeViewAt(idx)
            isAddView = false

            durationText?.isVisible = false
            duration = 0L
        }
    }

    fun releasePlayer() {
        removePlayerView()

        player?.run {
            removeListener(exoPlayerListener)
            release()
        }

        player = null
    }

    private fun playerStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {}
                ExoPlayer.STATE_BUFFERING -> {
                    progressBar?.isVisible = true
                }
                ExoPlayer.STATE_READY -> {
                    if (isAddView) {
                        removePlayerView()
                    }

                    addPlayerView()
                    progressBar?.isVisible = false
                }
                ExoPlayer.STATE_ENDED -> {
                    progressBar?.isVisible = false
                }
                else -> {}
            }
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)

            if (durationJob != null) {
                durationJob?.cancel()
            }

            player?.let { exoPlayer ->
                durationJob = exoCoroutineScope.launch {
                    if (reason == Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE) {
                        duration = exoPlayer.duration

                        if (duration < 0) return@launch

                        flow {
                            for (time in duration downTo 0) {
                                delay(1)
                                emit(DateUtil.convertMinuteAndSeconds(duration--))
                            }
                        }
                            .onEach { time -> durationText?.text = time }
                            .collect()
                    }
                }
            }
        }
    }

    private fun setDefaultHeight(context: Context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val bounds = windowManager.currentWindowMetrics.bounds
        val point = Point(bounds.width(), bounds.height())

        defaultExoPlayerHeight = point.x
        defaultScreenHeight = point.y
    }

    private fun getItem(position: Int): Video {
        return (adapter as PagingDataAdapter<*, *>).snapshot().items[position] as Video
    }

    private fun listSize(): Int {
        return (adapter as PagingDataAdapter<*, *>).snapshot().items.size
    }
}