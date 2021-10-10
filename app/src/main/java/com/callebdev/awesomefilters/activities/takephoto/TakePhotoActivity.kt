package com.callebdev.awesomefilters.activities.takephoto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.callebdev.awesomefilters.activities.editimage.EditImageActivity
import com.callebdev.awesomefilters.activities.main.MainActivity
import com.callebdev.awesomefilters.databinding.ActivityTakePhotoBinding
import com.callebdev.awesomefilters.utilities.Constants
import com.callebdev.awesomefilters.utilities.getOutputDirectory
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TakePhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTakePhotoBinding
    private lateinit var cameraExecutor: ExecutorService
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var photoFile: File
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted -> if (isGranted) startCamera() else onBackPressed() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory(resources)

        setupListeners()
        activityResultLauncher.launch(android.Manifest.permission.CAMERA)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnTakePhoto.setOnClickListener { takePhoto() }
        binding.btnFlipCamera.setOnClickListener { flipCamera() }
        binding.btnFlash.setOnCheckedChangeListener { _, isChecked ->
            imageCapture?.flashMode = if (isChecked) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderFuture.addListener(cameraProviderRunnableListener(cameraProviderFuture), ContextCompat.getMainExecutor(applicationContext))
    }

    private fun cameraProviderRunnableListener(cameraProviderFuture: ListenableFuture<ProcessCameraProvider>): Runnable {
        return Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().setTargetResolution(Size(Constants.IMAGE_CAPTURE_WIDTH, Constants.IMAGE_CAPTURE_HEIGHT)).build()

            try {
                // unbind use cases before rebinding
                cameraProvider.unbindAll()

                // bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.d(TAG, "cameraProviderRunnableListener: ${e.message}")
            }
        }
    }

    private fun takePhoto() {
        // Stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        photoFile = File(outputDirectory, System.currentTimeMillis().toString() + ".jpg")

        // output options object that contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        photoCaptureListener(imageCapture, outputOptions)
    }

    private fun flipCamera() {
        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        else if (cameraSelector === CameraSelector.DEFAULT_BACK_CAMERA) cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        startCamera()
    }

    // To be triggered after photoTaken
    private fun photoCaptureListener(imageCapture: ImageCapture, outputOptions: ImageCapture.OutputFileOptions) {
        imageCapture.takePicture(
            outputOptions, cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback(), ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    lifecycleScope.launch {
                        val imageUri = Uri.fromFile(photoFile)
                        Log.d(TAG, "onImageSaved: $imageUri")
                        Intent(applicationContext, EditImageActivity::class.java).also { editImageIntent ->
                            editImageIntent.putExtra(MainActivity.KEY_IMAGE_URI, imageUri)
                            startActivity(editImageIntent)
                        }
                        finish()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.d(TAG, "onError: Photo was not taken. Failed with exception: ${exception.message}")
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "TakePhotoActivity"
    }
}
