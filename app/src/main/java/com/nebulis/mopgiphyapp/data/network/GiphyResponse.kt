package com.nebulis.mopgiphyapp.data.network

import com.google.gson.annotations.SerializedName
import com.nebulis.mopgiphyapp.data.db.GifEntry

data class GiphyResponse(
    @SerializedName("data")
    val gifList: List<GifEntry>
)