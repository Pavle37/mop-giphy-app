package com.nebulis.mopgiphyapp.app

import android.app.Application
import com.nebulis.mopgiphyapp.data.db.GifDatabase
import com.nebulis.mopgiphyapp.data.network.GiphyRestClient
import com.nebulis.mopgiphyapp.data.repository.GifRepository
import com.nebulis.mopgiphyapp.data.repository.GifRepositoryImpl
import com.nebulis.mopgiphyapp.ui.grid.GridViewModelFactory
import org.kodein.di.*

class MopGiphyApp : Application(), DIAware {

    override val di = DI.lazy {
        bind() from singleton { GifDatabase(this@MopGiphyApp) }
        bind() from singleton { GiphyRestClient() }
        bind() from singleton { instance<GifDatabase>().getGifDao() }
        bind<GifRepository>() with singleton { GifRepositoryImpl(instance(),instance()) }
        bind() from factory { GridViewModelFactory(instance()) }
    }
}