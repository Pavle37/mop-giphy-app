package com.nebulis.mopgiphyapp.data.db.entity

import androidx.room.ColumnInfo

data class GifEntry(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "gif_preview_gif_url")
    val previewGifUrl: String,
    @ColumnInfo(name = "gif_downsized_large_url")
    val largeGifUrl: String
){
    override fun toString(): String {
        return "$title gifUrls: $largeGifUrl, $previewGifUrl"
    }
}