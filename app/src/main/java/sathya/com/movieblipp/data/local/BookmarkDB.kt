package sathya.com.movieblipp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sathya.com.movieblipp.data.local.dao.BookmarkDAO
import sathya.com.movieblipp.data.model.BookmarkedMovies

@Database(
    entities = [BookmarkedMovies::class],
    version = 1
)
abstract class BookmarkDB : RoomDatabase() {

    abstract fun getBookmarkDao(): BookmarkDAO

    companion object {

        private const val DATABASE_NAME = "BOOKMARK_DATABASE"

        @Volatile
        private var INSTANCE: BookmarkDB? = null

        fun getInstance(context: Context): BookmarkDB {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookmarkDB::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                return instance
            }
        }

    }

}