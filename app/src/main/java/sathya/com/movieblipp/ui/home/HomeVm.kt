package sathya.com.movieblipp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import sathya.com.movieblipp.data.model.Resource
import sathya.com.movieblipp.data.repo.ConnRepository
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeVm @Inject constructor(private val repository: ConnRepository) : ViewModel() {

    fun loadNowPlaying() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = repository.getNowPlayingMovie()
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

    fun loadUpcoming() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = repository.getUpcomingMovie()
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

    fun loadPopular() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = repository.getPopularMovie()
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

    fun loadTopRated() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = repository.getTopRatedMovie()
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


}