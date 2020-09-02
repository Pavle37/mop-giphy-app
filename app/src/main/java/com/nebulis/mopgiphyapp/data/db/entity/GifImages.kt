package com.nebulis.mopgiphyapp.data.db.entity

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class GifImages(
    @SerializedName("downsized_large")
    @Embedded(prefix = "downsized_large_")
    val gifLarge: GifImageUrl,
    @SerializedName("preview_gif")
    @Embedded(prefix = "preview_gif_")
    val gifPreview: GifImageUrl
)