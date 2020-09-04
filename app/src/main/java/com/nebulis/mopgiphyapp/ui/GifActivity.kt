package com.nebulis.mopgiphyapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.ui.grid.GridFragment


class GifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif)

        setSupportActionBar(findViewById(R.id.topAppBar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        menu ?: return true

        val actionItem = menu.findItem(R.id.search)
        val actionView = (actionItem.actionView as SearchView)
        actionView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        actionItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener{
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
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.childFragmentManager.fragments[0]
    }
}