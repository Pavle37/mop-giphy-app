package com.nebulis.mopgiphyapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nebulis.mopgiphyapp.data.db.dao.GifDao
import com.nebulis.mopgiphyapp.data.db.entity.GifEntity

private const val GIF_DATABASE_NAME = "gif-database.db"

@Database(entities = [GifEntity::class], version = 1)
abstract class GifDatabase : RoomDatabase(){

    abstract fun getGifDao(): GifDao

    companion object{
        /*Thread safe instance of singleton database*/
        @Volatile private var instance: GifDatabase? = null

        private val lock = Any() /*Generic object to keep the lock*/

        /*Override default create operator to create Singleton instance*/
        operator fun invoke(context: Context) = instance ?: synchronized(lock){
            instance ?: createDatabase(context.applicationContext).also {
                instance = it
            }
        }

        /**
         * Uses room database builder to create GIF_DATABASE_NAME file.
         *
         * @param appContext - app context because this is a singleton.
         */
        private fun createDatabase(appContext: Context): GifDatabase = Room.databaseBuilder(appContext,
            GifDatabase::class.java, GIF_DATABASE_NAME).build()
    }


}