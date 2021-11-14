package sathya.com.movieblipp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import sathya.com.movieblipp.data.model.Resource
import sathya.com.movieblipp.data.repo.ConnRepository
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(private val repository: ConnRepository) :
    ViewModel() {

    fun getPerson(person_id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch data from remote
            val apiResponse = repository.getPerson(person_id)
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

    fun getPersonMovieCredits(person_id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            // Fetch Data from Api
            val apiResponse = repository.getPersonMovieCredits(person_id)
            emit(Resource.success(apiResponse))
        } catch (e: Exception) {
            if (e is SocketTimeoutException) {
                emit(
                    Resource.error(
                        "Oops, something went wrong.\n" +
                                "Please try again later."
                    )
                )
            }
        }

    }


}