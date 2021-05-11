package com.callebdev.awesomefilters.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.callebdev.awesomefilters.data.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>

}