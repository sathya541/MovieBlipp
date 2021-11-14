package sathya.com.movieblipp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.databinding.ItemSearchBinding
import sathya.com.movieblipp.utils.Constants

class SearchRva() : PagingDataAdapter<Movie, SearchRva.ViewHolder>(
    MOVIE_COMPARATOR
) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSearchBinding.bind(itemView)

        fun bind(movie: Movie) {
            binding.apply {
                searchImage.load(BuildConfig.ORIGINAL_IMAGE_URL + movie.poster_path) {
                    placeholder(Constants.moviePlaceHolder[absoluteAdapterPosition % 4])
                    error(Constants.moviePlaceHolder[absoluteAdapterPosition % 4])
                }
                itemView.setOnClickListener {
                    val bundle = bundleOf(Constants.movie to movie)
                    it.findNavController()
                        .navigate(R.id.action_searchFragment_to_movieDetailsFragment, bundle)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }

    }


    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem

        }
    }


}