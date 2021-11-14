package sathya.com.movieblipp.ui.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.data.api.APIService
import sathya.com.movieblipp.data.model.Movie
import java.io.IOException

private const val DEFAULT_PAGE = 1

class PopularPagingSource(private val apiService: APIService) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        val position = params.key ?: DEFAULT_PAGE

        return try {

            val response = apiService.getPopularMovies(BuildConfig.API_KEY, position)
            val data = response.body()!!.results

            LoadResult.Page(
                data = data,
                prevKey = if (position == DEFAULT_PAGE) null else position - 1,
                nextKey = if (data.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int {
        return 1
    }

}