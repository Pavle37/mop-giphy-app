package com.nebulis.mopgiphyapp.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.ui.base.BaseScopedFragment
import com.xwray.groupie.GroupAdapter
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
    private lateinit var viewModel: GridViewModel

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
        viewModel.refreshTrending() /*Initial trending refresh*/

        initializeUI()
    }

    /**
     * Launches a coroutine on the main thread and initializes UI elements. Listens for updates
     * in the ViewModel and correspondingly updates the UI.
     */
    private fun initializeUI() = launch {
        initializeSwipeToRefresh()

        val groupieAdapter = initializeRecyclerView()

        viewModel.shownGifs.await().observe(viewLifecycleOwner, {
            val gridItems = it.toGridItems()
            groupieAdapter.updateAsync(gridItems)
        })

    }

    /**
     * Initializes swipe to refresh layout and sets listeners to handle refresh calls.
     */
    private fun initializeSwipeToRefresh() {
        viewModel.refreshListener.observe(viewLifecycleOwner,{
            swipeRefresh.isRefreshing = it /*Change values depending on the observed state*/
        })

        swipeRefresh.setOnRefreshListener {
            viewModel.refreshTrending()
        }
    }

    /**
     * Initializes adapter, layout manager and binds everything to recyclerview.
     */
    private fun initializeRecyclerView(): GroupAdapter<GroupieViewHolder> {
        val groupieAdapter = GroupAdapter<GroupieViewHolder>()
        val gridLm = StaggeredGridLayoutManager(NUMBER_OF_COLUMNS, RecyclerView.VERTICAL)

        rvGrid.apply {
            layoutManager = gridLm
            adapter = groupieAdapter
        }

        return groupieAdapter
    }
}
