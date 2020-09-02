package com.nebulis.mopgiphyapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nebulis.mopgiphyapp.data.db.entity.GifEntity
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry

@Dao
interface GifDao{

    /**
     * Returns defined amount of trending gifs starting at offset position.
     *
     * @param limit - max number of returned entries.
     * @param offset - starting position of the first entry.
     **/
    @Query("SELECT * FROM GifEntity where id between :offset and :limit")
    fun getGifs(limit: Int, offset: Int): LiveData<List<GifEntry>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg gifs: GifEntity)

    /**Clear all entries**/
    @Query("DELETE FROM GifEntity")
    fun clear()
}