package com.caiosilva.tagsearcher.data.api

import com.caiosilva.tagsearcher.data.model.ResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getImages(@Query("tags") tags: List<String>): ResponseData
}