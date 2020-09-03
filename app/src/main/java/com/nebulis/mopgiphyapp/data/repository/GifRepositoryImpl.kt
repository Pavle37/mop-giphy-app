package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import com.nebulis.mopgiphyapp.data.db.dao.GifDao
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.data.network.GiphyRestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val REFRESH_THRESHOLD = 60 * 1000 // 1m

/**
 * Implementation of GifRepository that handles Network and Database calls.
 */
class GifRepositoryImpl(
    private val gifDao: GifDao,
    private val giphyRestClient: GiphyRestClient
) : GifRepository {

    init {
        GlobalScope.launch(Dispatchers.IO) {
            refreshTrendingGifs()
        }
    }

    /*Flag that keeps last refresh time*/
    private var lastRefreshTime = -1L

    /**
     * Checks if parameters for refreshing are okay and if they are, initiates the request and updates
     * the databse with results.
     */
    override suspend fun refreshTrendingGifs() {
        val requestedRefreshTime = System.currentTimeMillis()
        if((requestedRefreshTime - lastRefreshTime) < REFRESH_THRESHOLD) return //Don't refresh

        lastRefreshTime = requestedRefreshTime

        initiateRefreshRequest()
    }

    /**
     * Tries to get fresh gifs from server. If successful clears out old ones and inserts new.
     */
    private suspend fun initiateRefreshRequest() {
        try {
            val trending = giphyRestClient.getTrendingGifs()

            /*On success, clear old ones and insert new*/
            gifDao.updateData(trending)

        }catch (e: Exception){
            /*Cannot refresh, ignore*/
            e.printStackTrace()
        }
    }

    override suspend fun getTrendingGifsLive(limit: Int, offset: Int): LiveData<List<GifEntry>> {
        return withContext(Dispatchers.IO){
            return@withContext gifDao.getGifs(limit,offset)
        }
    }
}