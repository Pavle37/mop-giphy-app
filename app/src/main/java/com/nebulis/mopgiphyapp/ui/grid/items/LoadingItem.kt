package com.nebulis.mopgiphyapp.ui.grid.items

import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.ui.grid.NUMBER_OF_COLUMNS
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

/**
 * Placeholder item that indicates that there are more items to be loaded in the grid.
 */
class LoadingItem: Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {}

    override fun getLayout(): Int = R.layout.loading_item

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return NUMBER_OF_COLUMNS
    }
}