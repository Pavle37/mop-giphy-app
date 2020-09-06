package com.nebulis.mopgiphyapp.data.db.entity

import androidx.room.ColumnInfo

data class GifEntry(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "gif_preview_gif_url")
    val previewGifUrl: String,
    @ColumnInfo(name = "gif_downsized_large_url")
    val largeGifUrl: String
) {
    override fun toString(): String {
        return "$title gifUrls: $largeGifUrl, $previewGifUrl"
    }

    companion object {
        /**
         * Converts entity object into an entry object.
         *
         * @param entity - entity we want to convert.
         */
        fun parse(entity: GifEntity): GifEntry {
            with(entity) {
                return GifEntry(title, gifImages.gifPreview.url, gifImages.gifLarge.url)
            }
        }
    }
}

fun List<GifEntity>.toGifEntry(): List<GifEntry> {
    val entries = mutableListOf<GifEntry>()
    this.mapTo(entries, {entity -> GifEntry.parse(entity)})
    return entries
}