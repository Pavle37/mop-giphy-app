package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nebulis.mopgiphyapp.data.db.dao.GifDao
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.data.db.entity.toGifEntry
import com.nebulis.mopgiphyapp.data.network.client.GiphyApiRestClient
import com.nebulis.mopgiphyapp.ui.grid.STARTING_OFFSET_POSITION
import com.nebulis.mopgiphyapp.util.forceRefresh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Implementation of GifRepository that handles Network and Database calls.
 */
class GifRepositoryImpl(
    private val gifDao: GifDao,
    private val giphyApiRestClient: GiphyApiRestClient
) : GifRepository {

    override val trendingGifs: LiveData<List<GifEntry>>
        get() = _trendingGifs

    private val _trendingGifs = MutableLiveData<List<GifEntry>>()

    override val searchedGifs: LiveData<List<GifEntry>>
        get() = _searchedGifs

    private val _searchedGifs = MutableLiveData<List<GifEntry>>()

    init {
        /**
         * Fetch gifs and observe database changes. Commit observations to trending live data.
         */
        gifDao.getGifsLive().observeForever {
            _trendingGifs.value = it
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
            val trending = giphyApiRestClient.getTrendingGifs(limit, offset)

            /*On success, clear old ones and insert new*/
            if (offset == STARTING_OFFSET_POSITION) gifDao.updateData(trending)
            /*On success, just insert*/
            else gifDao.insertAll(trending)
        }catch (e: Exception) {
            e.printStackTrace()
            /*Force refresh to indicate to observers that data change was not possible and to
            * update UI with the current data.*/
            _trendingGifs.forceRefresh()
        }
    }

    /**
     * Downloads searched gifs from backend. On success, commits to search results.
     *
     *  @param limit - max amount of entries to return.
     *  @param offset - starting index of the returned entry.
     *  @param query - string we want to search our gifs against.
     */
    @Suppress("UNCHECKED_CAST")
    override suspend fun updateSearchedGifs(limit: Int, offset: Int, query: String) {
        try {
            val searched = giphyApiRestClient.getSearchGifs(limit, offset,query).toGifEntry()

            /*On success, clear old ones and insert new*/
            if (offset == STARTING_OFFSET_POSITION) _searchedGifs.postValue(searched)
            /*On success, just insert*/
            else {
                val combined = mutableListOf<GifEntry>()
                combined.addAll(_searchedGifs.value!!)
                combined.addAll(searched)
                _searchedGifs.postValue(combined as List<GifEntry>)
            }
        }catch (e: Exception) {
            e.printStackTrace()
            /*Force refresh to indicate to observers that data change was not possible and to
            * update UI with the current data.*/
            _searchedGifs.forceRefresh()
        }
    }
}
