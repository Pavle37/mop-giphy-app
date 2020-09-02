package com.nebulis.mopgiphyapp.ui.grid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nebulis.mopgiphyapp.data.repository.GifRepository

/**
 * Factory that lets us supply our view model with the repository in DI.
 *
 * @param repo - repository from which view model will get data.
 */
class GridViewModelFactory(private val repo: GifRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GridViewModel(repo) as T
    }
}