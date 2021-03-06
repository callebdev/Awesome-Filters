package com.callebdev.awesomefilters.dependencyinjection

import com.callebdev.awesomefilters.repositories.EditImageRepository
import com.callebdev.awesomefilters.repositories.EditImageRepositoryImpl
import com.callebdev.awesomefilters.repositories.SavedImagesRepository
import com.callebdev.awesomefilters.repositories.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImagesRepository> { SavedImagesRepositoryImpl(androidContext()) }
}
