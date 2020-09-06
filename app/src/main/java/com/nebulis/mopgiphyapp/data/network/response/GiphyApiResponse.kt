package com.nebulis.mopgiphyapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.nebulis.mopgiphyapp.data.db.entity.GifEntity

data class GiphyApiResponse(
    @SerializedName("data")
    val gifList: List<GifEntity>
)