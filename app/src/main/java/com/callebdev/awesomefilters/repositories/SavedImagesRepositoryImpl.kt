package com.callebdev.awesomefilters.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import com.callebdev.awesomefilters.utilities.Constants
import java.io.File

class SavedImagesRepositoryImpl(private val context: Context) : SavedImagesRepository {

    override suspend fun loadSavedImages(): List<Pair<File, Bitmap>>? {
        val savedImages = ArrayList<Pair<File, Bitmap>>()

        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Constants.SAVED_IMAGES_DIRECTORY
        )
        dir.listFiles()?.let { data ->
            data.forEach { file ->
                savedImages.add(Pair(file, getPreviewBitmap(file)))
            }
            return savedImages
        } ?: return null
    }

    private fun getPreviewBitmap(file: File): Bitmap {
        val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val width = 150
        val height = ((originalBitmap.height * width) / originalBitmap.width)
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }
}
