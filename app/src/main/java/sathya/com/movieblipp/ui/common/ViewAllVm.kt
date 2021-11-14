package sathya.com.movieblipp.ui.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sathya.com.movieblipp.data.local.dao.BookmarkDAO
import sathya.com.movieblipp.data.model.BookmarkedMovies
import sathya.com.movieblipp.data.repo.ConnRepository
import javax.inject.Inject

@HiltViewModel
class ViewAllVm @Inject constructor(
    private val databaseDAO: BookmarkDAO, private val repository: ConnRepository
) : ViewModel() {

    private val _bookmarks = MutableLiveData<List<BookmarkedMovies>>()

    var bookmarks: MutableLiveData<List<BookmarkedMovies>> = _bookmarks

    fun fetchPopular() =
        repository.getPopularMovieResult().cachedIn(viewModelScope)

    fun fetchUpcoming() =
        repository.getUpcomingMovieResult().cachedIn(viewModelScope)

    fun fetchTopRated() =
        repository.getTopRatedMovieResult().cachedIn(viewModelScope)

    fun fetchBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = databaseDAO.getAllBookmark()
            bookmarks.postValue(data)
        }
    }

}