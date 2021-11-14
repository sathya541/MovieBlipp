package sathya.com.movieblipp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.databinding.ItemSimilarMovieBinding
import sathya.com.movieblipp.utils.Constants

class SimilarMoviesRva(val context: Context, val movies: ArrayList<Movie>) :
    RecyclerView.Adapter<SimilarMoviesRva.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemSimilarMovieBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_similar_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            if (position == 0) {
                binding.spacingStart.visibility = View.VISIBLE
            } else if (position == movies.size - 1) {
                binding.spacingEnd.visibility = View.VISIBLE
            } else {
                binding.spacingEnd.visibility = View.GONE
                binding.spacingStart.visibility = View.GONE
            }

            binding.movieImage.load(BuildConfig.ORIGINAL_IMAGE_URL + movies[position].poster_path) {
                placeholder(Constants.moviePlaceHolder[absoluteAdapterPosition % 4])
                error(Constants.moviePlaceHolder[absoluteAdapterPosition % 4])
            }

            binding.movieImage.setOnClickListener {
                val bundle = bundleOf(Constants.movie to movies[position])
                it.findNavController().navigate(R.id.action_movieDetailsFragment_self, bundle)
            }
        }
    }

    override fun getItemCount(): Int = movies.size

}