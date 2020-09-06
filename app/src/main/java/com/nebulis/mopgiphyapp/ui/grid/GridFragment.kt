package com.nebulis.mopgiphyapp.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.ui.GifActivity
import com.nebulis.mopgiphyapp.ui.base.BaseScopedFragment
import com.nebulis.mopgiphyapp.ui.grid.items.GridItem
import com.nebulis.mopgiphyapp.ui.grid.items.LoadingItem
import com.nebulis.mopgiphyapp.ui.grid.items.toGridItems
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.grid_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

/*Number of columns in our recyclerview*/
const val NUMBER_OF_COLUMNS = 2

/**
 * View class that displays a grid of gifs. Handles calls for pull to refresh, load more and search.
 */
class GridFragment : BaseScopedFragment(), DIAware {

    override val di: DI by di()
    private val factory: GridViewModelFactory by instance() /*Inject the factory*/
    internal lateinit var viewModel: GridViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.grid_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, factory).get(GridViewModel::class.java)

        initializeUI()
    }

    /**
     * Launches a coroutine on the main thread and initializes UI elements. Listens for updates
     * in the ViewModel and correspondingly updates the UI.
     */
    @Suppress("UNCHECKED_CAST")
    private fun initializeUI() = launch {
        initializeSwipeToRefresh()

        val groupieAdapter = initializeRecyclerView()

        viewModel.startOffsetObserving()
        viewModel.shownGifs.observe(viewLifecycleOwner, {
            val gridItems = it.toGridItems()
            groupieAdapter.update(gridItems)
            isLoading = false
        })

        groupieAdapter.setOnItemClickListener { item, _ ->
            if(item is GridItem) {
                (activity as GifActivity).openGifFullscreen(item.gif.largeGifUrl)
            }
        }

    }

    /**
     * Initializes swipe to refresh layout and sets listeners to handle refresh calls.
     */
    private fun initializeSwipeToRefresh() {
        viewModel.refreshListener.observe(viewLifecycleOwner, {
            swipeRefresh.isRefreshing = it /*Change values depending on the observed state*/
        })

        swipeRefresh.setOnRefreshListener {
            launch {
                viewModel.refreshTrending()
            }
        }
    }

    /**
     * Indicates if load more has been initiated. Pauses the scroll while true.
     */
    private var isLoading = false


    /**
     * Initializes adapter, layout manager and binds everything to recyclerview.
     */
    private fun initializeRecyclerView(): GroupAdapter<GroupieViewHolder> {
        val groupieAdapter = GroupAdapter<GroupieViewHolder>()
        val gridLm = StaggeredGridLayoutManager(NUMBER_OF_COLUMNS, RecyclerView.VERTICAL)

        /*Listen to scroll events and handle them*/
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handleScroll(gridLm,groupieAdapter)
                return
            }
        }

        /*Wire everything up*/
        rvGrid.apply {
            layoutManager = gridLm
            adapter = groupieAdapter
            addOnScrollListener(scrollListener)
        }

        return groupieAdapter
    }

    /**
     * Looks for the last visible element and if it matches loading, initiates load more request.
     *
     * @param gridLm - layout manager used to get visible item positions.
     * @param groupieAdapter - adapter used to handle fetching of the items.
     */
    private fun handleScroll(gridLm: StaggeredGridLayoutManager,groupieAdapter: GroupAdapter<GroupieViewHolder>) {
        if (isLoading) return
        val positions = IntArray(NUMBER_OF_COLUMNS)
        val visiblePositions = gridLm.findLastCompletelyVisibleItemPositions(positions)
        val groupCount = groupieAdapter.groupCount
        val visibleItem1 = groupieAdapter.getItem(visiblePositions[0])
        val visibleItem2 = groupieAdapter.getItem(visiblePositions[1])

        if (visibleItem1 is LoadingItem || visibleItem2 is LoadingItem) {
            isLoading = true
            viewModel.loadMore(groupCount - 1)
        }
    }
}
