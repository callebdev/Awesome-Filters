package com.callebdev.awesomefilters.dependencyinjection

import com.callebdev.awesomefilters.viewmodels.EditImageViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory <EditImageViewModel>  {
        EditImageViewModel(editImageRepository = get())
    }
}