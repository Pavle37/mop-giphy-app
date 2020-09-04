package com.nebulis.mopgiphyapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {

    @GET("gifs/trending")
    suspend fun getTrendingGifs(@Query("limit") limit: Int,
                                @Query("offset") offset:Int,
                                @Query("rating") rating: String = "g"): GiphyResponse

    @GET("gifs/search")
    suspend fun getSearchGifs(@Query("limit") limit: Int,
                                @Query("offset") offset:Int,
                                @Query("rating") rating: String = "g",
                                @Query("q") query: String,
                                @Query("lang") language: String = "en"): GiphyResponse


}