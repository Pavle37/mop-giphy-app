package com.nebulis.mopgiphyapp.data.network.client

import android.content.Context
import android.net.Uri
import com.nebulis.mopgiphyapp.data.network.response.GiphyUploadResponse
import com.nebulis.mopgiphyapp.data.network.service.GiphyUploadService
import com.nebulis.mopgiphyapp.util.RequestBodyParser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "upload.giphy.com/v1/"
private const val PROTOCOL = "https://"

private const val FILE_FIELD_NAME = "file"

/**
 * Class that instantiates GiphyApiService with retrofit and encapsulates request fetching logic.
 */
class GiphyUploadRestClient(private val service: GiphyUploadService) {

    companion object {

        /*Thread safe instance of singleton*/
        @Volatile var instance: GiphyUploadRestClient? = null
            private set

        private val lock = Any() /*Generic object to keep the lock*/

        operator fun invoke() = synchronized(lock) {
            instance ?: createService().also { instance = it }
        }

        /**
         * Creates GiphyApiService and attaches to the instance of this rest client.
         */
        private fun createService(): GiphyUploadRestClient {

            val logginInterceptor = HttpLoggingInterceptor()
            logginInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logginInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build()

            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient).baseUrl(PROTOCOL + BASE_URL).build()

            val service = retrofit.create(GiphyUploadService::class.java)

            return GiphyUploadRestClient(service)
        }
    }

    /**
     * Creates request body parts and adds them to upload request and initiates it.
     *
     * @return Server response
     */
    suspend fun uploadImage(context: Context, videoUri: Uri, name: String, type: String): GiphyUploadResponse {
        val apiKey = RequestBodyParser.parseString(API_KEY)!!
        val file = RequestBodyParser.parseUri(context,FILE_FIELD_NAME,videoUri,name,type)

        return service.uploadVideo(apiKey,file)
    }

}