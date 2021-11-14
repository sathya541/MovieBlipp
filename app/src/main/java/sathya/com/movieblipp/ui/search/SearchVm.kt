package sathya.com.movieblipp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import sathya.com.movieblipp.data.model.Resource
import sathya.com.movieblipp.data.repo.ConnRepository
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SearchVm @Inject constructor(private val connRepository: ConnRepository) : ViewModel() {

    fun searchMovie(query: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val apiResponse =
                connRepository.getSearchResult(query).cachedIn(viewModelScope)
            emit(Resource.success(apiResponse))
        } catch (e: Exception) {
            if (e is SocketTimeoutException)
                emit(Resource.error("Oops, something went wrong.\nPlease Try again later."))
        }
    }

    fun getSearchMovie(query: String) =
        connRepository.getSearchResult(query).cachedIn(viewModelScope)


}