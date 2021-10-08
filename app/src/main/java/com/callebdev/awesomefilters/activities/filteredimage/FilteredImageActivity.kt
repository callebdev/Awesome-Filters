package com.callebdev.awesomefilters.activities.filteredimage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.callebdev.awesomefilters.activities.editimage.EditImageActivity
import com.callebdev.awesomefilters.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilteredImageBinding

    private lateinit var fileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilteredImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupListeners()
    }

    private fun setupUi() {
        intent.getParcelableExtra<Uri>(EditImageActivity.KEY_FILTERED_IMAGE_URI)?.let {
            fileUri = it
            binding.imageFilteredImage.setImageURI(it)
        }
    }

    private fun setupListeners() {
        binding.fabShareFilteredImage.setOnClickListener {
            with(Intent(Intent.ACTION_SEND)) {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
    }
}
