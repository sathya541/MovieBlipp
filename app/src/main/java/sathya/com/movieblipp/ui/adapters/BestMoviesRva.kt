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
import sathya.com.movieblipp.databinding.ItemMovieHomeBinding
import sathya.com.movieblipp.utils.Constants

class BestMoviesRva(val context: Context, val movies: ArrayList<Movie>) :
    RecyclerView.Adapter<BestMoviesRva.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemMovieHomeBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movie_home, parent, false)
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
                placeholder(Constants.moviePlaceHolder[position % 4])
                error(Constants.moviePlaceHolder[position % 4])
            }
            binding.textMovieName.text = movies[position].title
            binding.textMovieRating.text = movies[position].vote_average.toString()

            itemView.setOnClickListener {
                val bundle = bundleOf(Constants.movie to movies[position])
                it.findNavController()
                    .navigate(R.id.action_actorDetailsFragment_to_movieDetailsFragment, bundle)
            }

            if (position == movies.size - 1) {
                binding.spacingEnd.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount() = movies.size


}