package com.nebulis.mopgiphyapp.data.db.entity

import androidx.room.ColumnInfo

data class GifImageUrl(
    @ColumnInfo(name = "url")
    val url: String
)