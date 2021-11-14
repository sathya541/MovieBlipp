package sathya.com.movieblipp.data.local.dao

import androidx.room.*
import sathya.com.movieblipp.data.model.BookmarkedMovies
import sathya.com.movieblipp.utils.Constants.Companion.TABLE_NAME

@Dao
interface BookmarkDAO {

    /**
     * Fetch all movies
     */
    @Query("Select * from $TABLE_NAME")
    fun getAllBookmark(): List<BookmarkedMovies>

    /**
     * Check movie exist in DB
     */
    @Query("SELECT EXISTS (SELECT 1 FROM $TABLE_NAME WHERE id = :id)")
    fun bookmarkExist(id: Int): Boolean

    /**
     * Insert [movie] into the Movie Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: BookmarkedMovies)

    /**
     * Delete movie from Database
     */
    @Delete
    suspend fun removeMovie(movie: BookmarkedMovies)

    /**
     * Delete [Movie] by [Movie.id]
     */
    @Query("Delete from ${TABLE_NAME} where id=:id")
    suspend fun deleteMovieById(id: Int)


}