package com.callebdev.awesomefilters.listeners

import com.callebdev.awesomefilters.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}