package com.caiosilva.tagsearcher.data.remote

import com.caiosilva.tagsearcher.data.model.ErrorResponse
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

object NetworkHelper {
    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apicall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            return@withContext try {
                ResultWrapper.Success(apicall.invoke())
            } catch (throwable: Throwable) {
                return@withContext when (throwable) {
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ResultWrapper.GenericError(code, errorResponse)
                    }

                    is IOException -> ResultWrapper.NetworkError(
                        ErrorResponse(
                            throwable.cause?.message ?: throwable.message ?: "Unknown error",
                            throwable.cause?.message ?: throwable.message ?: "Unknown error",
                            mapOf(),
                            throwable.cause?.message ?: throwable.message ?: "Unknown error",
                        )
                    )

                    else -> {
                        ResultWrapper.GenericError(
                            null, ErrorResponse(
                                throwable.cause?.message ?: throwable.message ?: "Unknown error",
                                throwable.cause?.message ?: throwable.message ?: "Unknown error",
                                mapOf(),
                                throwable.cause?.message ?: throwable.message ?: "Unknown error",
                            )
                        )
                    }
                }
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }
}