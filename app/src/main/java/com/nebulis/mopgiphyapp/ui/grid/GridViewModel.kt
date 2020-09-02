package com.nebulis.mopgiphyapp.ui.grid

import androidx.lifecycle.ViewModel
import com.nebulis.mopgiphyapp.data.repository.GifRepository
import com.nebulis.mopgiphyapp.util.lazyDeferred

class GridViewModel(repo: GifRepository) : ViewModel() {

    val shownGifs by lazyDeferred {
        repo.getTrendingGifsLive(20,0)
    }

}