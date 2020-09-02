package com.nebulis.mopgiphyapp.data.network

import com.google.gson.annotations.SerializedName
import com.nebulis.mopgiphyapp.data.db.entity.GifEntity

data class GiphyResponse(
    @SerializedName("data")
    val gifList: List<GifEntity>
)