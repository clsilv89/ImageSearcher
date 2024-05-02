package com.caiosilva.tagsearcher

import com.caiosilva.tagsearcher.data.model.ErrorResponse
import com.caiosilva.tagsearcher.data.model.ResponseData
import com.caiosilva.tagsearcher.data.remote.ResultWrapper
import com.caiosilva.tagsearcher.usecases.IImagesUseCaseImpl
import com.caiosilva.tagsearcher.view.viewmodel.ImagesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ImagesViewModelTest {
    private lateinit var viewModel: ImagesViewModel
    private lateinit var useCase: IImagesUseCaseImpl

    @Before
    fun setUp() {
        useCase = mockk()
        viewModel = ImagesViewModel(useCase)
    }

    @Test
    fun `updateQuery should update query and call getImages`() = runTest {
        val query = "cats"
        viewModel.updateQuery(query)

        assertEquals(query, viewModel.query.first())
    }

    @Test
    fun `getImages should update isLoading, responseData and errorMessage on success`() =
        runTest {
            val query = "cats"
            val response = ResponseData("id", "url", "url", "url", "url", arrayListOf())

            coEvery { useCase.invoke(listOf(query)) } returns ResultWrapper.Success(response)
            viewModel.updateQuery(query)

            assertTrue(viewModel.isLoading.first())
            assertEquals(response, viewModel.responseData.first())
            assertFalse(viewModel.isLoading.first())
        }


    @Test
    fun `getImages should update isLoading and errorMessage on GenericError`() = runTest {
        val query = "cats"
        val error = ErrorResponse("error", "error", mapOf(), "error")
        coEvery { useCase.invoke(any()) } returns ResultWrapper.GenericError(400, error)

        viewModel.updateQuery(query)

        assertTrue(viewModel.isLoading.first())
        assertEquals(error.message, viewModel.errorMessage.first())
    }

    @Test
    fun `getImages should update isLoading and errorMessage on NetworkError`() = runTest {
        val query = "cats"
        val error = ResultWrapper.NetworkError(
            ErrorResponse("error", "error", mapOf(), "error")
        )
        coEvery { useCase.invoke(any()) } returns ResultWrapper.NetworkError(error.error!!)

        viewModel.updateQuery(query)

        assertTrue(viewModel.isLoading.first())
        assertEquals(error.error!!.errorDescription, viewModel.errorMessage.first())
    }
}