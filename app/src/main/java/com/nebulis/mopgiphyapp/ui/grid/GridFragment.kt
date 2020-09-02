package com.nebulis.mopgiphyapp.ui.grid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.ui.base.BaseScopedFragment
import kotlinx.android.synthetic.main.grid_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.di
import org.kodein.di.instance

class GridFragment : BaseScopedFragment(), DIAware {

    override val di: DI by di()
    private val factory: GridViewModelFactory by instance()
    private lateinit var viewModel: GridViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.grid_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this,factory).get(GridViewModel::class.java)

        launch {
            viewModel.shownGifs.await().observe(viewLifecycleOwner, {
                tvHello.text = it.toString()
            })

        }
    }

}

