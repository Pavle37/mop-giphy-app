package com.nebulis.mopgiphyapp.data.network.service

import com.nebulis.mopgiphyapp.data.network.response.GiphyUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface GiphyUploadService {

    @Multipart
    @POST("gifs")
    suspend fun uploadVideo(@Part("api_key")apiKey: RequestBody,
                            @Part file: MultipartBody.Part) : GiphyUploadResponse

}