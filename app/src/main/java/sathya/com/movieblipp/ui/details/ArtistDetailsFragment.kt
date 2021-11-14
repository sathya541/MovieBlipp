package sathya.com.movieblipp.ui.details

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
import sathya.com.movieblipp.BuildConfig
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Crew
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.data.model.Status
import sathya.com.movieblipp.databinding.FragmentArtistDetailsBinding
import sathya.com.movieblipp.ui.adapters.BestMoviesRva
import sathya.com.movieblipp.utils.Constants
import sathya.com.movieblipp.utils.showToast

@AndroidEntryPoint
class ArtistDetailsFragment : Fragment() {

    private val viewModel: ArtistDetailsViewModel by viewModels()
    private lateinit var binding: FragmentArtistDetailsBinding
    private lateinit var crew: Crew
    private var movieCredits: ArrayList<Movie> = ArrayList()
    private lateinit var homeRva: BestMoviesRva
    private lateinit var skeletonBestMovies: Skeleton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_artist_details, container, false)
        binding = FragmentArtistDetailsBinding.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        crew = requireArguments().get(Constants.crew) as Crew

        binding.buttonBack.setOnClickListener {
            it.findNavController().navigateUp()
        }

        initAdapters()

        fetchData()

    }

    private fun fetchData() {
        viewModel.getPerson(crew.id).observe(requireActivity(), Observer {
            val actor = it.data
            if (actor != null) {
                binding.textActorName.text = actor.name
                binding.actorImage.load(BuildConfig.ORIGINAL_IMAGE_URL + actor.profile_path)

                var knownAs = ""
                for (i in 0 until Math.min(4, actor.also_known_as.size)) {
                    knownAs += actor.also_known_as[i]
                    if (i != actor.also_known_as.size - 1) {
                        knownAs += ", "
                    }
                }
                binding.textAlsoKnownAs.text = knownAs

                binding.textBio.text = actor.biography

                binding.textPopularity.text = actor.popularity.toString()
                binding.textCharacter.text = actor.known_for_department
                binding.textBirthday.text = actor.birthday
            }
        })

        viewModel.getPersonMovieCredits(crew.id).observe(requireActivity(), Observer { res ->
            when (res.status) {
                Status.LOADING -> {
                    skeletonBestMovies.showSkeleton()
                }
                Status.SUCCESS -> {
                    skeletonBestMovies.showOriginal()
                    movieCredits.clear()
                    movieCredits.addAll(res.data!!.cast)
                    homeRva.notifyDataSetChanged()
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

    private fun initAdapters() {
        homeRva = BestMoviesRva(requireContext(), movieCredits)
        binding.recyclerViewBestMovies.adapter = homeRva
        skeletonBestMovies =
            binding.recyclerViewBestMovies.applySkeleton(R.layout.item_movie_home, 10)
    }

}