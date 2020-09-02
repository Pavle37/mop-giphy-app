package com.nebulis.mopgiphyapp.data.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "api.giphy.com/v1/"
private const val PROTOCOL = "https://"

private const val API_KEY = "cBBqDDZS66OwzOo3bNu076sD8t0sZuRy"

class GiphyRestClient(val service: GiphyApiService) {

    companion object {

        /*Thread safe instance of singleton*/
        @Volatile
        var instance: GiphyRestClient? = null
            private set

        private val lock = Any() /*Generic object to keep the lock*/

        operator fun invoke() = synchronized(lock) {
            instance ?: createService().also { instance = it }
        }

        /**
         * Creates GiphyApiService and attaches to the instance of this rest client.
         */
        private fun createService(): GiphyRestClient {
            /*
            * Intercepts any call to Giphy API and adds api_key query parameter to the request chain.
            */
            val apiKeyInterceptor = Interceptor { chain ->

                /*Get current url and append the api key*/
                val url = chain.request().url.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                /*Create new request and add it to chain*/
                val newRequest = chain.request().newBuilder().url(url).build()
                return@Interceptor chain.proceed(newRequest)
            }
            val logginInterceptor = HttpLoggingInterceptor()
            logginInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor)
                .addInterceptor(logginInterceptor)
                .build()

            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).baseUrl(PROTOCOL + BASE_URL).build()

            val service = retrofit.create(GiphyApiService::class.java)

            return GiphyRestClient(service)
        }
    }
}