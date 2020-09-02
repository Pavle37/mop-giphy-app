package com.nebulis.mopgiphyapp.data.repository

import androidx.lifecycle.LiveData
import com.nebulis.mopgiphyapp.data.db.dao.GifDao
import com.nebulis.mopgiphyapp.data.db.entity.GifEntity
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.data.db.entity.GifImageUrl
import com.nebulis.mopgiphyapp.data.db.entity.GifImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GifRepositoryImpl(
    private val gifDao: GifDao
) : GifRepository {

    init {
        GlobalScope.launch(Dispatchers.IO) {
            insertMockedGif()
        }
    }

    private fun insertMockedGif() {
        val mockedGif = GifEntity(1,GifImages(GifImageUrl("url1"),GifImageUrl("url2")),"Mocked Gif")
        gifDao.insertAll(mockedGif)
    }

    override suspend fun getTrendingGifs(limit: Int, offset: Int): LiveData<List<GifEntry>> {
        return withContext(Dispatchers.IO){
            return@withContext gifDao.getGifs(limit,offset)
        }
    }
}