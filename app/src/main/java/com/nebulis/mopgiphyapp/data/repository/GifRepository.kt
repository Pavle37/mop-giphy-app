package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.ui.grid.LIMIT_ITEMS
import com.nebulis.mopgiphyapp.ui.grid.STARTING_OFFSET_POSITION

interface GifRepository {

    /**
     * Trending gifs observable data.
     */
    val trendingGifs: LiveData<List<GifEntry>>

    /**
     * Searched gifs observable data.
     */
    val searchedGifs: LiveData<List<GifEntry>>

    /**
     * Updates the trending gifs starting at offset and going until the limit.
     *
     * @param limit - max number of returned entries.
     * @param offset - starting position of the first entry.
     **/
    suspend fun updateTrendingGifs(
        limit: Int = LIMIT_ITEMS,
        offset: Int = STARTING_OFFSET_POSITION,
    )

    /**
     * Updates the searched gifs starting at offset and going until the limit.
     *
     * @param limit - max number of returned entries.
     * @param offset - starting position of the first entry.
     *  @param query - string we want to search our gifs against.
     **/
    suspend fun updateSearchedGifs(
        limit: Int = LIMIT_ITEMS,
        offset: Int = STARTING_OFFSET_POSITION,
        query: String
    )

}