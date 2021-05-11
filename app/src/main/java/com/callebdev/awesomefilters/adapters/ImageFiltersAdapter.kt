package com.callebdev.awesomefilters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callebdev.awesomefilters.data.ImageFilter
import com.callebdev.awesomefilters.databinding.ItemContainerFilterBinding

class ImageFiltersAdapter(private val imageFilters: List<ImageFilter>): RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
            }
        }
    }

    override fun getItemCount() = imageFilters.size

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) : RecyclerView.ViewHolder(binding.root)
}