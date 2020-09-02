package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry

interface GifRepository {

    /**
     * Returns defined amount of trending gifs starting at offset position.
     *
     * @param limit - max number of returned entries.
     * @param offset - starting position of the first entry.
     **/
    suspend fun getTrendingGifsLive(limit: Int, offset: Int) : LiveData<List<GifEntry>>

    /**
     * Tries to get fresh gifs from server. If successful clears out old ones and inserts new.
     */
    suspend fun refreshTrendingGifs()
}