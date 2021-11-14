package sathya.com.movieblipp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.load
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.databinding.FragmentHomeViewPagerBinding
import sathya.com.movieblipp.utils.Constants


class HomeVpFragment(val movie: Movie) : Fragment() {

    private lateinit var binding: FragmentHomeViewPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_view_pager, container, false)
        binding = FragmentHomeViewPagerBinding.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.viewPagerImage.load(BuildConfig.ORIGINAL_IMAGE_URL + movie.backdrop_path) {
            placeholder(Constants.viewPagerPlaceHolder.random())
            error(Constants.viewPagerPlaceHolder.random())
        }
        binding.viewPagerText.text = movie.title

        binding.root.setOnClickListener {
            val bundle = bundleOf(Constants.movie to movie)
            it.findNavController()
                .navigate(R.id.action_homeFragment_to_movieDetailsFragment, bundle)
        }

    }
}