package com.caiosilva.tagsearcher.usecases

import com.caiosilva.tagsearcher.data.model.ResponseData
import com.caiosilva.tagsearcher.data.remote.ResultWrapper

interface IImagesUseCase {
    suspend fun invoke(tags: List<String>): ResultWrapper<ResponseData>
}