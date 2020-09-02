package com.nebulis.mopgiphyapp.data.network

import com.google.gson.annotations.SerializedName

data class GifImages(
    @SerializedName("downsized_large")
    val gifLarge: GifImageUrl,
    @SerializedName("preview_gif")
    val gifPreview: GifImageUrl
)