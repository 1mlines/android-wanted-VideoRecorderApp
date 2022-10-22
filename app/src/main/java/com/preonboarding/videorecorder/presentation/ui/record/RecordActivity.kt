package com.preonboarding.videorecorder.presentation.ui.record

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.preonboarding.videorecorder.R
import com.preonboarding.videorecorder.databinding.ActivityRecordBinding
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class RecordActivity : BaseActivity<ActivityRecordBinding>() {

    override val layoutResourceId: Int = R.layout.activity_record
    private val viewModel: RecordViewModel by viewModels()

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private var lensFacing = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnChangeMirror.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            startCamera()
        }

        binding.videoCaptureButton.setOnClickListener {
            captureVideo()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        binding.videoCaptureButton.isEnabled = false

        // 진행 중인 녹음이 있으면 중지, 현재 녹음을 해제한다.
        // 캡처한 비디오 파일이 애플리케이션에서 사용할 준비가 됐을 때 알림을 받게 된다.
        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            return
        }

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        // videoCapture 의 레코더에 대한 출력 옵션을 구성하고 오디오 녹음 활성화
        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        this@RecordActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) ==
                    PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        binding.videoCaptureButton.apply {
                            text = "stop capture"
                            isEnabled = true
                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            viewModel.saveVideo(
                                Video(
                                    recordEvent.outputResults.outputUri.toString(),
                                    SimpleDateFormat(
                                        FILENAME_FORMAT,
                                        Locale.KOREA
                                    ).format(System.currentTimeMillis())
                                )
                            )
                        } else {
                            recording?.close()
                            recording = null
                        }
                        binding.videoCaptureButton.apply {
                            text = "start capture"
                            isEnabled = true
                        }
                    }
                }
            }
    }

    private fun startCamera() {
        // 카메라 생명 주기를 현재 라이프사이클 오너에게 연결하는데 사용된다.
        // CameraX 는 생명 주기를 인식하므로 카메라를 열고 닫는 작업이 필요하지 않다.
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        // 리스너 추가
        cameraProviderFuture.addListener({

            // 애플리케이션 프로세스 내에서 카메라의 생명주기를 라이프사이클 오너에 바인딩하는데 사용된다.
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview 클래스 생성
            val preview: Preview = Preview.Builder()
                .build()
                .also {
                    // 유즈케이스(Preview)와 previewView 를 연결
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Recorder 클래스 생성
            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll() // 이전에 바인딩된 유즈케이스 해제
                cameraProvider
                    .bindToLifecycle(
                        this as LifecycleOwner,
                        lensFacing,
                        preview,
                        videoCapture
                    ) // 라이프 사이클 연결
            } catch (e: Exception) {
                Toast.makeText(this, "Use case binding failed $e", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd HH-mm-ss"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}
