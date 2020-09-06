package com.nebulis.mopgiphyapp.ui.grid.items

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nebulis.mopgiphyapp.R
import com.nebulis.mopgiphyapp.data.db.entity.GifEntry
import com.nebulis.mopgiphyapp.ui.grid.NUMBER_OF_COLUMNS
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.gif_grid_item.view.*


/**
 * Class that binds our grid item layout with GifEntry.
 */
class GridItem(val gif: GifEntry): Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val ivPicture = viewHolder.root.ivPicture
        Glide.with(viewHolder.root).asGif().load(gif.previewGifUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivPicture)
    }

    override fun getLayout(): Int = R.layout.gif_grid_item

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return NUMBER_OF_COLUMNS
    }

    override fun equals(other: Any?): Boolean {
        return (other as GridItem).compareTo(this.gif.title)
    }

    private fun compareTo(title: String): Boolean {
        return this.gif.title == title
    }

    override fun hashCode(): Int {
        return gif.hashCode()
    }

    override fun isSameAs(other: Item<*>): Boolean {
        return if((other is GridItem)) other.compareTo(gif.title)
                else false
    }
}

/**
 * Converts GifEntry into GridItem.
 */
fun  List<GifEntry>.toGridItems(): List<Item<GroupieViewHolder>> {
    val gridItems = mutableListOf<Item<GroupieViewHolder>>()
    this.mapTo(gridItems, { entry -> GridItem(entry) })
    gridItems.add(LoadingItem())

    return gridItems
}