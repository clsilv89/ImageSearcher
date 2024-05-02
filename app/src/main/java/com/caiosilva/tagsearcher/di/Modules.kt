package com.caiosilva.tagsearcher.di

import com.caiosilva.tagsearcher.BuildConfig
import com.caiosilva.tagsearcher.data.api.Api
import com.caiosilva.tagsearcher.data.remote.NetworkHelper
import com.caiosilva.tagsearcher.data.remote.RestConfig
import com.caiosilva.tagsearcher.data.repository.ImagesRepository
import com.caiosilva.tagsearcher.usecases.IImagesUseCase
import com.caiosilva.tagsearcher.usecases.IImagesUseCaseImpl
import com.caiosilva.tagsearcher.view.viewmodel.ImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Modules {
    val module = module {
        factory<Api> { RestConfig.service(BuildConfig.BASE_URL) }
        single { NetworkHelper }
        single { ImagesRepository(get(), get()) }
        single<IImagesUseCase> { IImagesUseCaseImpl(get()) }
        viewModel { ImagesViewModel(get()) }
    }
}