package sathya.com.movieblipp.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import dagger.hilt.android.AndroidEntryPoint
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.databinding.FragmentViewAllBinding
import sathya.com.movieblipp.ui.adapters.BookmarkRva
import sathya.com.movieblipp.ui.adapters.ViewAllRva
import sathya.com.movieblipp.utils.Constants

@AndroidEntryPoint
class ViewAllFragment : Fragment() {

    private val viewAllVm: ViewAllVm by viewModels()
    private lateinit var binding: FragmentViewAllBinding
    private lateinit var movieAllRva: ViewAllRva
    private lateinit var movieSkeleton: Skeleton

    private var moviesList: ArrayList<Movie> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_all, container, false)
        binding = FragmentViewAllBinding.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movieAllRva = ViewAllRva()
        binding.movieRecyclerView.adapter = movieAllRva

        movieSkeleton = binding.movieRecyclerView.applySkeleton(R.layout.item_search, 15)

        val pageType = requireArguments().get(Constants.viewAll)
        binding.pageTitle.text = pageType.toString()
        when (pageType) {
            Constants.Upcoming -> fetchUpcoming()
            Constants.TopRated -> fetchTopRated()
            Constants.Popular -> fetchPopular()
            Constants.Bookmarks -> fetchBookmarks()
        }

        binding.buttonBack.setOnClickListener {
            binding.root.findNavController().navigateUp()
        }

    }

    fun fetchBookmarks() {

        viewAllVm.bookmarks.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.movieRecyclerView.adapter = BookmarkRva(it)
            }
        }

        viewAllVm.fetchBookmarks()

    }

    fun fetchPopular() {
        viewAllVm.fetchPopular().observe(viewLifecycleOwner) {

            movieAllRva.submitData(viewLifecycleOwner.lifecycle, it)

        }
    }

    fun fetchTopRated() {

        viewAllVm.fetchTopRated().observe(viewLifecycleOwner) {

            movieAllRva.submitData(viewLifecycleOwner.lifecycle, it)

        }

    }

    fun fetchUpcoming() {

        viewAllVm.fetchUpcoming().observe(viewLifecycleOwner) {

            movieAllRva.submitData(viewLifecycleOwner.lifecycle, it)

        }

    }

}