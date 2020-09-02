package com.nebulis.mopgiphyapp.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GifEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") /*because field is transient*/
    @Transient /*Ignore by Gson*/
    val id: Int,
    @SerializedName("images")
    @Embedded(prefix = "gif_")
    val gifImages: GifImages,
    val title: String
)