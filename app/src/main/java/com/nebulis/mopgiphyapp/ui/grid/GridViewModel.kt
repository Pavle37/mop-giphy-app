package com.nebulis.mopgiphyapp.ui.grid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nebulis.mopgiphyapp.data.repository.GifRepository
import com.nebulis.mopgiphyapp.util.lazyDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

private const val REFRESH_THRESHOLD = 60 * 1000 // 1m


class GridViewModel(private val repo: GifRepository) : ViewModel() {

    val shownGifs by lazyDeferred {
        repo.getTrendingGifsLive(20, 0)
    }

    /**
     * Observable element that indicates the state of refreshing.
     */
    val refreshListener = MutableLiveData(true) /*Refresh true indicating "getTrendingGifsLive*/

    /*Flag that keeps last refresh time*/
    private var lastRefreshTime = -1L

    /**
     * Checks if parameters for refreshing are okay and if they are, initiates the request and updates
     * the database with results.
     */
    fun refreshTrending() {
        val requestedRefreshTime = System.currentTimeMillis()
        if ((requestedRefreshTime - lastRefreshTime) < REFRESH_THRESHOLD){
            refreshListener.value = false
            return
        } //Don't refresh

        lastRefreshTime = requestedRefreshTime
        refreshListener.value = true

        initiateRefreshRequest()
    }

    /**
     * Refreshes trending gifs and updates shownGifs data.
     */
    private fun initiateRefreshRequest() = GlobalScope.launch(Dispatchers.IO) {/*ViewModel is lifecycle element, so GlobalScope is alright*/
        try {
            repo.refreshTrendingGifs()
        } catch (e: Exception) {
            /*Cannot refresh, ignore*/
            e.printStackTrace()
        }finally {
            GlobalScope.launch(Dispatchers.Main) {/*Set value must not be invoked on a BG thread*/
                refreshListener.value = false
            }
        }
    }

}