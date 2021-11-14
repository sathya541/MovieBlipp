package sathya.com.movieblipp.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.data.api.APIService
import sathya.com.movieblipp.data.api.ConnApiRequest
import sathya.com.movieblipp.ui.pagination.PopularPagingSource
import sathya.com.movieblipp.ui.pagination.SearchPagingSource
import sathya.com.movieblipp.ui.pagination.TopRatedPagingSource
import sathya.com.movieblipp.ui.pagination.UpcomingPagingSource
import javax.inject.Inject

class ConnRepository @Inject constructor(private val apiService: APIService) : ConnApiRequest() {

    // Get Movie Details
    suspend fun getMovieDetails(movie_id: Int) = apiRequest {
        apiService.getMovieById(movie_id, BuildConfig.API_KEY)
    }

    // Now Playing Movies
    suspend fun getNowPlayingMovie() = apiRequest {
        apiService.getNowPlayingMovies(BuildConfig.API_KEY)
    }

    // Upcoming Movies
    suspend fun getUpcomingMovie() = apiRequest {
        apiService.getUpcomingMovies(BuildConfig.API_KEY)
    }

    // Popular Movies
    suspend fun getPopularMovie() = apiRequest {
        apiService.getPopularMovies(BuildConfig.API_KEY)
    }

    // Top Rated Movies
    suspend fun getTopRatedMovie() = apiRequest {
        apiService.getTopRatedMovies(BuildConfig.API_KEY)
    }

    // Movie Credits
    suspend fun getMovieCredits(movie_id: Int) = apiRequest {
        apiService.getMovieCredits(movie_id, BuildConfig.API_KEY)
    }

    // Get Similar Movies
    suspend fun getSimilarMovies(movie_id: Int) = apiRequest {
        apiService.getSimilarMovies(movie_id, BuildConfig.API_KEY)
    }

    // Get Person
    suspend fun getPerson(personId: Int) = apiRequest {
        apiService.getPerson(personId, BuildConfig.API_KEY)
    }

    // Person Movie Credits
    suspend fun getPersonMovieCredits(personId: Int) = apiRequest {
        apiService.getPersonMovieCredits(personId, BuildConfig.API_KEY)
    }

    // Search Movie
    suspend fun searchMovie(query: String, page: Int) = apiRequest {
        apiService.searchMovie(query, page, BuildConfig.API_KEY)
    }

    fun getSearchResult(query: String) =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            pagingSourceFactory = {
                SearchPagingSource(apiService, query)
            }
        ).liveData

    fun getPopularMovieResult() =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            pagingSourceFactory = {
                PopularPagingSource(apiService)
            }
        ).liveData

    fun getUpcomingMovieResult() =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            pagingSourceFactory = {
                UpcomingPagingSource(apiService)
            }
        ).liveData

    fun getTopRatedMovieResult() =
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 500),
            pagingSourceFactory = {
                TopRatedPagingSource(apiService)
            }
        ).liveData

}