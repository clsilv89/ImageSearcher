package com.caiosilva.tagsearcher.usecases

import com.caiosilva.tagsearcher.data.model.ResponseData
import com.caiosilva.tagsearcher.data.remote.ResultWrapper
import com.caiosilva.tagsearcher.data.repository.ImagesRepository

class IImagesUseCaseImpl (
    private val repository: ImagesRepository
) : IImagesUseCase {
    override suspend fun invoke(tags: List<String>): ResultWrapper<ResponseData> =
        repository.getImages(tags)
}