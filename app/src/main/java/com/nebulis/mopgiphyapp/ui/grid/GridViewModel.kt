package com.nebulis.mopgiphyapp.ui.grid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.data.repository.GifRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val REFRESH_THRESHOLD = 60 * 1000 // 1m
const val LIMIT_ITEMS = 20
const val STARTING_OFFSET_POSITION = 0

class GridViewModel(private val repo: GifRepository) : ViewModel() {

    /**
     * List of current gifs to be displayed in the grid.
     */
    val shownGifs: LiveData<List<GifEntry>>
        get() = _shownGifs

    /*Private mutable implementation*/
    private val _shownGifs = MutableLiveData<List<GifEntry>>()

    /*Offset observable*/
    private val offset = MutableLiveData(STARTING_OFFSET_POSITION)

    private var gifContext = ShownGifs.TRENDING

    init {
        /*Attach observer and handle changes in trending gifs data*/
        repo.trendingGifs.observeForever { trending ->
            /*Number of items that should be showing currently*/
            val shownItems = offset.value!! + LIMIT_ITEMS
            if (shownItems > trending.size) return@observeForever //Cannot load more
            /*Post UI changes for the given subset of values*/
            _shownGifs.value = trending.subList(0, shownItems)
        }

        repo.searchedGifs.observeForever {searched ->
            /*Number of items that should be showing currently*/
            val shownItems = offset.value!! + LIMIT_ITEMS
            if (shownItems > searched.size) return@observeForever //Cannot load more
            /*Post UI changes for the given subset of values*/
            _shownGifs.value = searched.subList(0, shownItems)
        }
    }

    /**
     * Starts to observe offset changes. First run initiates refresh()
     */
    fun startOffsetObserving() {
        lastRefreshTime = System.currentTimeMillis()

        offset.observeForever {
            GlobalScope.launch(Dispatchers.Main) {
                if(gifContext == ShownGifs.TRENDING) {
                    repo.updateTrendingGifs(LIMIT_ITEMS, it)
                }else{
                    repo.updateSearchedGifs(LIMIT_ITEMS, it,searchedQuery)
                }
                refreshListener.value = false
            }
        }
    }

    /**
     * Observable element that indicates the state of refreshing.
     */
    val refreshListener = MutableLiveData(false) /*Refresh true indicating "getTrendingGifsLive*/

    /*Flag that keeps last refresh time*/
    private var lastRefreshTime = -1L

    /**
     * Checks if parameters for refreshing are okay and if they are, initiates the request and updates
     * the database with results.
     */
    fun refreshTrending() {
        Log.d("TrendingGifs", "refresh called")
        val requestedRefreshTime = System.currentTimeMillis()
        if ((requestedRefreshTime - lastRefreshTime) < REFRESH_THRESHOLD) {
            refreshListener.value = false
            return
        } //Don't refresh

        lastRefreshTime = requestedRefreshTime

        offset.value = STARTING_OFFSET_POSITION
    }

    /**
     * Loads more items from the API with given offset.
     *
     * @param offset - from which item position should we start the load.
     */
    fun loadMore(offset: Int) {
        this.offset.value = offset
    }


    private var searchedQuery = ""

    /**
     * Contacts the repository to update the search gifs. Changes context so showGifs will display
     * searched items.
     *
     * @param query - text we want to search the gifs with.
     */
    fun performSearch(query: String) {
        gifContext = ShownGifs.SEARCHED
        searchedQuery = query
        offset.value = STARTING_OFFSET_POSITION
    }

    /**
     * Clears searches and refreshes trending gifs.
     */
    fun clearSearch() {
        gifContext = ShownGifs.TRENDING
        refreshTrending()
    }

}

private enum class ShownGifs{
    TRENDING, SEARCHED
}