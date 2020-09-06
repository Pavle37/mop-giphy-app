package com.nebulis.mopgiphyapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.ui.grid.GridFragment
import com.nebulis.mopgiphyapp.ui.grid.GridFragmentDirections
import com.nebulis.mopgiphyapp.ui.upload.GENERIC_VIDEO_TYPE
import com.nebulis.mopgiphyapp.ui.upload.VideoUploader
import kotlinx.android.synthetic.main.activity_gif.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


private const val REQUEST_VIDEO = 37


class GifActivity : AppCompatActivity(), DIAware {

    override val di: DI by di()
    private val  videoUploader: VideoUploader by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)

        setSupportActionBar(findViewById(R.id.topAppBar))

        fabUpload.setOnClickListener {
            launchUploadPhotoIntent()
        }
    }

    private fun launchUploadPhotoIntent() {
        val intent = Intent()
        intent.type = GENERIC_VIDEO_TYPE
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            REQUEST_VIDEO
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO) {
                val videoUri = data?.data ?: return
                videoUploader.uploadVideoFromUri(videoUri)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        menu ?: return true

        val actionItem = menu.findItem(R.id.search)
        val actionView = (actionItem.actionView as SearchView)
        actionView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        actionItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                clearSearch()
                return true
            }
        })

        return true
    }

    /**
     * Clears search results and refreshes trending gifs.
     */
    private fun clearSearch() {
        getCurrentFragment().let {
            if(it is GridFragment) it.viewModel.clearSearch()
        }
    }


    /**
     * Only query handling done in onNewIntent because we don't want to handle outside app calls.
     *
     * @param intent - object containing our search query.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)


           getCurrentFragment().let {
                if(it is GridFragment) it.viewModel.performSearch(query ?: "")
            }
        }
    }

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment: NavHostFragment = getNavFragment()
        return navHostFragment.childFragmentManager.fragments[0]
    }

    private fun getNavFragment(): NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

    /**
     * Opens full screen gif fragment.
     *
     * @param url - url of the gif we want to show.
     */
    fun openGifFullscreen(url: String) {
        val action = GridFragmentDirections.actionGridFragmentToFullscreenFragment(url)
        getNavFragment().navController.navigate(action)
    }
}

