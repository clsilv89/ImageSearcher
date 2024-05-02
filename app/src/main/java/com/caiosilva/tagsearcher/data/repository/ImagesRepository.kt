package com.caiosilva.tagsearcher.data.repository

import com.caiosilva.tagsearcher.data.api.Api
import com.caiosilva.tagsearcher.data.remote.NetworkHelper
import kotlinx.coroutines.Dispatchers

class ImagesRepository (
    private val api: Api,
    private val networkHelp: NetworkHelper
) {
    private val dispatcher = Dispatchers.IO

    suspend fun getImages(tags: List<String>) = networkHelp.safeApiCall(dispatcher) {
        api.getImages(tags)
    }
}