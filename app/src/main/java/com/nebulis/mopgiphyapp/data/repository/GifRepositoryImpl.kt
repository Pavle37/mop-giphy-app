package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nebulis.mopgiphyapp.data.db.dao.GifDao
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.data.network.GiphyRestClient
import com.nebulis.mopgiphyapp.ui.grid.STARTING_OFFSET_POSITION
import com.nebulis.mopgiphyapp.util.forceRefresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Implementation of GifRepository that handles Network and Database calls.
 */
class GifRepositoryImpl(
    private val gifDao: GifDao,
    private val giphyRestClient: GiphyRestClient
) : GifRepository {

    override val trendingGifs: LiveData<List<GifEntry>>
        get() = _treningGifs

    private val _treningGifs = MutableLiveData<List<GifEntry>>()

    init {
        /**
         * Fetch gifs and observe database changes. Commit observations to trending live data.
         */
        gifDao.getGifsLive().observeForever {
            _treningGifs.value = it
        }
    }

    /**
     * Downloads trending gifs from backend. On success, commits to database. Either way emits
     * live data signal when it's completed.
     *
     *  @param limit - max amount of entries to return.
     *  @param offset - starting index of the returned entry.
     */
    override suspend fun updateTrendingGifs(limit: Int, offset: Int) = withContext(Dispatchers.IO){
        try {
            val trending = giphyRestClient.getTrendingGifs(limit, offset)

            /*On success, clear old ones and insert new*/
            if (offset == STARTING_OFFSET_POSITION) gifDao.updateData(trending)
            /*On success, just insert*/
            else gifDao.insertAll(trending)
        }catch (e: Exception) {
            e.printStackTrace()
            /*Force refresh to indicate to observers that data change was not possible and to
            * update UI with the current data.*/
            _treningGifs.forceRefresh()
        }
    }
}