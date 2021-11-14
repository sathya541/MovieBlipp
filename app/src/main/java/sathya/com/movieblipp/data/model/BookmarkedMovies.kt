package sathya.com.movieblipp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import sathya.com.movieblipp.utils.Constants

@Entity(tableName = Constants.TABLE_NAME)
data class BookmarkedMovies(
    @PrimaryKey
    val id: Int,
    val poster_path: String,
    val overview: String,
    val title: String,
    val backdrop_path: String
)