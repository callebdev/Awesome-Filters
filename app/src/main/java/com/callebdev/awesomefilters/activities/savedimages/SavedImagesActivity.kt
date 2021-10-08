package com.callebdev.awesomefilters.activities.savedimages

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.callebdev.awesomefilters.activities.editimage.EditImageActivity
import com.callebdev.awesomefilters.activities.filteredimage.FilteredImageActivity
import com.callebdev.awesomefilters.adapters.SavedImagesAdapter
import com.callebdev.awesomefilters.databinding.ActivitySavedImagesBinding
import com.callebdev.awesomefilters.listeners.SavedImageListener
import com.callebdev.awesomefilters.utilities.displayToast
import com.callebdev.awesomefilters.viewmodels.SavedImagesViewModel
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedImagesActivity : AppCompatActivity(), SavedImageListener {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
        viewModel.loadSavedImages()
    }

    private fun setupObservers() {
        viewModel.savedImagesUiState.observe(this) { it ->
            val savedImagesDataState = it ?: return@observe

            binding.savedImagesProgressBar.visibility = if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE

            savedImagesDataState.savedImages?.let { savedImages ->
                SavedImagesAdapter(savedImages, this).also { savedImagesAdapter ->
                    with(binding.savedImagesRecyclerView) {
                        this.adapter = savedImagesAdapter
                        visibility = View.VISIBLE
                    }
                }
            } ?: run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setupUi() {
        setSupportActionBar(binding.toolbarSavedImages)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbarSavedImages.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(applicationContext, "${packageName}.provider", file)
        Intent(applicationContext, FilteredImageActivity::class.java).also {
            it.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(it)
        }
    }
}
