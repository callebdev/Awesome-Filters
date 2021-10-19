package com.callebdev.awesomefilters.activities.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.callebdev.awesomefilters.activities.editimage.EditImageActivity
import com.callebdev.awesomefilters.activities.savedimages.SavedImagesActivity
import com.callebdev.awesomefilters.activities.takephoto.TakePhotoActivity
import com.callebdev.awesomefilters.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1
        const val KEY_IMAGE_URI = "imageUri"
    }

    private lateinit var binding: ActivityMainBinding

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { onImagePicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonEditImageFromGallery.setOnClickListener {

            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ).also { pickerIntent ->
                pickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                resultLauncher.launch(pickerIntent)
            }
        }

        binding.buttonViewSavedImages.setOnClickListener {
            Intent(applicationContext, SavedImagesActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.buttonTakePhoto.setOnClickListener {
            Intent(applicationContext, TakePhotoActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun onImagePicked(activityResult: ActivityResult) {
        if (activityResult.resultCode == RESULT_OK) {
            activityResult.data?.data?.let { imageUri ->
                Intent(applicationContext, EditImageActivity::class.java).also { editImageIntent ->
                    editImageIntent.putExtra(KEY_IMAGE_URI, imageUri)
                    startActivity(editImageIntent)
                }
            }
        }
    }
}
