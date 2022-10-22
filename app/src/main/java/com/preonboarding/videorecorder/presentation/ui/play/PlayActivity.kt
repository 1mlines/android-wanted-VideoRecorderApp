package com.preonboarding.videorecorder.presentation.ui.play

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.preonboarding.videorecorder.R
import timber.log.Timber

class PlayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val model = intent.getStringExtra("video")
        Timber.tag("asdf").e(model.toString())
    }
}