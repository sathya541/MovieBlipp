package sathya.com.movieblipp.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import coil.load
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Crew
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.data.model.Status
import sathya.com.movieblipp.databinding.FragmentMovieDetailsBinding
import sathya.com.movieblipp.ui.adapters.CastRva
import sathya.com.movieblipp.ui.adapters.SimilarMoviesRva
import sathya.com.movieblipp.utils.Constants
import sathya.com.movieblipp.utils.showToast
import sathya.com.movieblipp.utils.toHours

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(), View.OnClickListener {

    private lateinit var movie: Movie

    private val viewModel: MovieDetailsViewModel by viewModels()
    private lateinit var binding: FragmentMovieDetailsBinding

    private var crewList: ArrayList<Crew> = ArrayList()
    private var similarList: ArrayList<Movie> = ArrayList()

    private lateinit var castRva: CastRva
    private lateinit var similarRva: SimilarMoviesRva

    private lateinit var castSkeleton: Skeleton
    private lateinit var similarMovieSkeleton: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        binding = FragmentMovieDetailsBinding.bind(view)
        return view
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movie = requireArguments().get(Constants.movie) as Movie

        viewModel.movieName.value = movie.title
        viewModel.movie.value = movie

        binding.appBarLayout.buttonBack.setOnClickListener(this)
        binding.appBarLayout.bookmarks.setOnClickListener(this)

        initAdapters()

        loadData()

        loadCast()

        loadSimilar()

        checkBookmark()

        viewModel.getMovieDetails(movie.id)

    }

    private fun initAdapters() {
        similarRva = SimilarMoviesRva(requireContext(), similarList)
        castRva = CastRva(requireContext(), crewList)

        binding.recyclerViewCast.adapter = castRva
        binding.recyclerViewRelated.adapter = similarRva

        castSkeleton = binding.recyclerViewCast.applySkeleton(R.layout.item_cast, 10)
        similarMovieSkeleton =
            binding.recyclerViewRelated.applySkeleton(R.layout.item_similar_movie, 10)
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {

        viewModel.movie.observe(requireActivity(), Observer {

            var genre: String = ""
            if (!it.genres.isNullOrEmpty())
                for (i in 0..it.genres.size - 1) {
                    genre += Constants.getGenreMap()[it.genres[i].id].toString()
                    if (i != it.genres.size - 1) {
                        genre += "• "
                    }
                }

            binding.apply {
                textMovieName.text = it!!.title
                textRating.text = "${it.vote_average}/10"
                textReleaseDate.text = it.release_date
                textDescription.text = it.overview
                if (it.runtime != null)
                    textLength.text = toHours(it.runtime!!)
                textGenres.text = genre

                detailsBannerImage.load(BuildConfig.ORIGINAL_IMAGE_URL + it.backdrop_path) {
                    placeholder(Constants.viewPagerPlaceHolder.random())
                    error(Constants.viewPagerPlaceHolder.random())
                }

                imagePoster.load(BuildConfig.ORIGINAL_IMAGE_URL + it.poster_path) {
                    placeholder(Constants.moviePlaceHolder.random())
                    error(Constants.moviePlaceHolder.random())
                }
            }

        })

    }

    private fun loadSimilar() {
        viewModel.loadSimilar(movie.id).observe(requireActivity(), Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (similarList.isNotEmpty())
                        similarMovieSkeleton.showSkeleton()
                }
                Status.SUCCESS -> {
                    similarList.clear()
                    similarList.addAll(it.data!!.results)
                    similarRva.notifyDataSetChanged()
                    similarMovieSkeleton.showOriginal()

                    if (similarList.isNullOrEmpty()) {
                        binding.headingRelated.visibility = View.GONE
                    } else {
                        binding.headingRelated.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    showToast(
                        "Oops, something went wrong.\n" +
                                "Please try again later."
                    )
                }
            }
        })
    }

    private fun loadCast() {
        viewModel.loadCast(movie.id).observe(requireActivity(), Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (crewList.isNullOrEmpty())
                        castSkeleton.showSkeleton()
                }
                Status.SUCCESS -> {
                    castSkeleton.showOriginal()
                    crewList.clear()
                    crewList.addAll(it.data!!.crew)
                    castRva.notifyDataSetChanged()

                    if (crewList.isNullOrEmpty()) {
                        binding.headingCast.visibility = View.GONE
                    } else {
                        binding.headingCast.visibility = View.VISIBLE
                    }

                }
                Status.ERROR -> {
                    showToast(
                        "Oops, something went wrong.\n" +
                                "Please try again later."
                    )
                }
            }
        })
    }

    fun checkBookmark() {

        viewModel.bookmark.observe(viewLifecycleOwner, Observer {
            binding.apply {
                if (it) {
                    bookmarks.setImageResource(R.drawable.ic_bookmark_done)
                } else {
                    bookmarks.setImageResource(R.drawable.ic_bookmark)
                }
            }
        })

        viewModel.checkBookmarkExist()

    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.buttonBack -> {
                binding.root.findNavController().navigateUp()
            }
            R.id.bookmarks -> {
                viewModel.bookmarkMovie()
                viewModel.checkBookmarkExist()
            }
        }

    }

}