package com.caiosilva.tagsearcher.view.viewmodel

import androidx.lifecycle.ViewModel
import com.caiosilva.tagsearcher.data.model.ResponseData
import com.caiosilva.tagsearcher.data.remote.ResultWrapper
import com.caiosilva.tagsearcher.usecases.IImagesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ImagesViewModel(
    private val useCase: IImagesUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _responseData = MutableStateFlow<ResponseData?>(null)
    val responseData = _responseData.asStateFlow()

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    fun updateQuery(query: String) {
        _query.value = query
        getImages(query)
    }

    fun getImages(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.emit(true)
            when (val result = useCase.invoke(query.split(",").toList())) {
                is ResultWrapper.Success -> {
                    _isLoading.emit(false)
                    _responseData.emit(result.value)
                }

                is ResultWrapper.GenericError -> {
                    _errorMessage.emit(result.error?.message ?: "Unknown error")
                    handleError(result.error?.message ?: "Unknown error")
                }

                is ResultWrapper.NetworkError -> {
                    _errorMessage.emit(result.error?.message ?: "Network error")
                    handleError(result.toString())
                }
            }
        }
    }

    private fun handleError(error: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.emit(false)
            _errorMessage.emit(error)
        }
    }
}