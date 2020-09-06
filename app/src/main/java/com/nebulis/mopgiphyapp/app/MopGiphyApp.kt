package com.nebulis.mopgiphyapp.app

import android.app.Application
import com.nebulis.mopgiphyapp.data.db.GifDatabase
import com.nebulis.mopgiphyapp.data.network.client.GiphyApiRestClient
import com.nebulis.mopgiphyapp.data.network.client.GiphyUploadRestClient
import com.nebulis.mopgiphyapp.data.repository.GifRepository
import com.nebulis.mopgiphyapp.data.repository.GifRepositoryImpl
import com.nebulis.mopgiphyapp.ui.grid.GridViewModelFactory
import com.nebulis.mopgiphyapp.ui.upload.VideoUploader
import com.nebulis.mopgiphyapp.ui.upload.VideoUploaderImpl
import org.kodein.di.*

class MopGiphyApp : Application(), DIAware {

    override val di = DI.lazy {
        bind() from singleton { GifDatabase(this@MopGiphyApp) }
        bind() from singleton { GiphyApiRestClient() }
        bind() from singleton { GiphyUploadRestClient() }
        bind() from singleton { instance<GifDatabase>().getGifDao() }
        bind<GifRepository>() with singleton { GifRepositoryImpl(instance(),instance()) }
        bind() from factory { GridViewModelFactory(instance()) }
        bind<VideoUploader>() with factory { VideoUploaderImpl(this@MopGiphyApp,instance()) }
    }
}