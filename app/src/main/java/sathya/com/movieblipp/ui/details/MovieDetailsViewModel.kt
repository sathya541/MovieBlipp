package sathya.com.movieblipp.ui.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sathya.com.movieblipp.data.local.dao.BookmarkDAO
import sathya.com.movieblipp.data.model.BookmarkedMovies
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.data.model.Resource
import sathya.com.movieblipp.data.repo.ConnRepository
import sathya.com.movieblipp.utils.Constants
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val databaseDAO: BookmarkDAO, private val connRepository: ConnRepository
) : ViewModel() {

    private val _name = MutableLiveData("Movie Name")
    private val _movie = MutableLiveData<Movie>()

    var bookmark = MutableLiveData(false)
    var movieName: MutableLiveData<String> = _name
    var movie: MutableLiveData<Movie> = _movie

    fun getMovieDetails(movie_id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiResponse = connRepository.getMovieDetails(movie_id)
            movie.postValue(apiResponse)
        }
    }

    fun loadCast(movie_id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = connRepository.getMovieCredits(movie_id)
            emit(Resource.success(apiResponse))
        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                emit(
                    Resource.error(
                        "Oops, something went wrong.\n" +
                                "Please try again later."
                    )
                )
        }
    }

    fun loadSimilar(movie_id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = connRepository.getSimilarMovies(movie_id)
            emit(Resource.success(apiResponse))
        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                emit(
                    Resource.error(
                        "Oops, something went wrong.\n" +
                                "Please try again later."
                    )
                )
        }
    }


    fun bookmarkMovie() {
        movie.value!!.apply {
            val movieDb = BookmarkedMovies(id, poster_path!!, overview!!, title!!, backdrop_path!!)
            viewModelScope.launch(Dispatchers.IO) {
                if (bookmark.value == true) {
                    databaseDAO.removeMovie(movieDb)
                } else {
                    databaseDAO.insertMovie(movieDb)
                }
            }
            Log.i(Constants.TAG, "Bookmark")
        }
    }

    fun checkBookmarkExist() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = databaseDAO.bookmarkExist(movie.value!!.id)
            bookmark.postValue(response)
        }
    }

}