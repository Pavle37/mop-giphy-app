package com.nebulis.mopgiphyapp.data.network.response

import com.google.gson.annotations.SerializedName

data class GiphyUploadResponse(
    @SerializedName("meta")
    val metaData: MetaData
)