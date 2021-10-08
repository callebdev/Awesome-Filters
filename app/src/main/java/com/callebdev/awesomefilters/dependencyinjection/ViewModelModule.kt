package com.callebdev.awesomefilters.dependencyinjection

import com.callebdev.awesomefilters.viewmodels.EditImageViewModel
import com.callebdev.awesomefilters.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}
