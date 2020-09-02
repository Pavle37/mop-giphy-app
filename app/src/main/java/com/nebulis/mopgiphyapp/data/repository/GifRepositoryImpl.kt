package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import com.nebulis.mopgiphyapp.data.db.dao.GifDao
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.data.network.GiphyRestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GifRepositoryImpl(
    private val gifDao: GifDao,
    private val giphyRestClient: GiphyRestClient
) : GifRepository {

    init {
        GlobalScope.launch(Dispatchers.IO) {
            refreshTrendingGifs()
        }
    }

    /**
     * Tries to get fresh gifs from server. If successful clears out old ones and inserts new.
     */
    override suspend fun refreshTrendingGifs() {
        try {
            val trending = giphyRestClient.service.getTrendingGifs()

            /*On success, clear old ones and insert new*/
            gifDao.updateData(trending.gifList)

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