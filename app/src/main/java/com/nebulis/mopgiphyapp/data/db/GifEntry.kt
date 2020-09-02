package com.nebulis.mopgiphyapp.data.db

import com.google.gson.annotations.SerializedName
import com.nebulis.mopgiphyapp.data.network.GifImages

data class GifEntry(
    val id: String,
    @SerializedName("images")
    val gifImages: GifImages,
    val title: String,
    val type: String
)