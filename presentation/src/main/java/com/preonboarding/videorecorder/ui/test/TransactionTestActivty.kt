package com.preonboarding.videorecorder.ui.test

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.preonboarding.videorecorder.databinding.ActivityTransactionTestActivtyBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionTestActivty : AppCompatActivity() {

    private val binding: ActivityTransactionTestActivtyBinding by lazy { ActivityTransactionTestActivtyBinding.inflate(layoutInflater) }
    private val transactionViewModel: TransactionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(binding.root)
        binding.button.setOnClickListener {
            transactionViewModel.insertVideoData("")
        }
    }
}
