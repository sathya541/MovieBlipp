package sathya.com.movieblipp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.BookmarkedMovies
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.databinding.ItemSearchBinding
import sathya.com.movieblipp.utils.Constants

class BookmarkRva(val movies: List<BookmarkedMovies>) :
    RecyclerView.Adapter<BookmarkRva.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSearchBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {

            Glide.with(itemView)
                .load("${BuildConfig.ORIGINAL_IMAGE_URL}${movies[position].poster_path}")
                .placeholder(Constants.moviePlaceHolder[position % 4])
                .error(Constants.moviePlaceHolder[position % 4])
                .into(binding.searchImage)

            binding.root.setOnClickListener {
                val movie = Movie(
                    id = movies[position].id,
                    title = movies[position].title,
                    backdrop_path = movies[position].backdrop_path,
                    poster_path = movies[position].poster_path,
                    overview = movies[position].overview
                )
                val bundle = bundleOf(Constants.movie to movie)
                it.findNavController()
                    .navigate(R.id.action_viewAllFragment_to_movieDetailsFragment, bundle)
            }

        }

    }

    override fun getItemCount() = movies.size


}