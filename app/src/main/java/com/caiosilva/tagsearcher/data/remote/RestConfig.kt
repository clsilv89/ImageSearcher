package com.caiosilva.tagsearcher.data.remote

import com.caiosilva.tagsearcher.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestConfig {
    private const val TIMEOUT = 20L

    internal inline fun <reified S> service(url: String): S {
        return Retrofit
            .Builder()
            .baseUrl(url)
            .extras()
            .client()
            .build()
            .create(S::class.java)
    }

    private fun Retrofit.Builder.client(): Retrofit.Builder {
        val okhttpClient = OkHttpClient.Builder()
            .timeout()
            .interceptor()
            .build()

        return client(okhttpClient)
    }

    private fun Retrofit.Builder.extras(): Retrofit.Builder {
        return addConverterFactory(GsonConverterFactory.create())
    }

    private fun OkHttpClient.Builder.timeout(): OkHttpClient.Builder {
        return readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT, TimeUnit.SECONDS)
    }

    private fun OkHttpClient.Builder.interceptor(): OkHttpClient.Builder {
        return addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }).addInterceptor { chain ->
            val request = chain.request()
            val newRequest = request
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
    }
}
