package com.callebdev.awesomefilters.utilities

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import com.callebdev.awesomefilters.R
import java.io.File

fun Context.displayToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Activity.getOutputDirectory(resources: Resources): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).also { file ->
            file.mkdirs()
        }
    }

    return if (mediaDir != null && mediaDir.exists()) mediaDir else this.filesDir
}
