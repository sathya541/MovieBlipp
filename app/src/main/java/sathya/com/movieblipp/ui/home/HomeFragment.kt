package sathya.com.movieblipp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import dagger.hilt.android.AndroidEntryPoint
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.data.model.Status
import sathya.com.movieblipp.databinding.FragmentHomeBinding
import sathya.com.movieblipp.ui.adapters.HomeRva
import sathya.com.movieblipp.ui.adapters.HomeVpa
import sathya.com.movieblipp.utils.Constants
import sathya.com.movieblipp.utils.showToast
import java.lang.Math.abs

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController

    private val vm: HomeVm by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private var upcomingMovieList: ArrayList<Movie> = ArrayList()
    private var popularMovieList: ArrayList<Movie> = ArrayList()
    private var topRatedMovieList: ArrayList<Movie> = ArrayList()

    private lateinit var upcomingAdapter: HomeRva
    private lateinit var popularAdapter: HomeRva
    private lateinit var topRatedAdapter: HomeRva

    private lateinit var viewPagerSkeleton: Skeleton
    private lateinit var upcomingSkeleton: Skeleton
    private lateinit var topRatedSkeleton: Skeleton
    private lateinit var popularSkeleton: Skeleton

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navController = Navigation.findNavController(binding.root)
        binding.homeViewPager.setPageTransformer { page, position ->
            page.translationX = -10 * position
            page.scaleY = 1 - (0.25f * abs(position))
        }

        binding.homeSearchButton.setOnClickListener(this)
        binding.bookmarks.setOnClickListener(this)
        binding.textViewAllPopular.setOnClickListener(this)
        binding.textViewAllTopRated.setOnClickListener(this)
        binding.textViewAllUpcoming.setOnClickListener(this)

        initAdapters()

        initSkeletons()

        fetchData()

    }

    private fun fetchData() {
        vm.loadNowPlaying().observe(requireActivity(), Observer { res ->
            when (res.status) {
                Status.LOADING -> {
                    viewPagerSkeleton.showSkeleton()
                }
                Status.SUCCESS -> {
                    viewPagerSkeleton.showOriginal()
                    binding.homeViewPager.adapter =
                        HomeVpa(childFragmentManager, lifecycle, res.data!!.results)
                }
                Status.ERROR -> {
                    showToast(res.msg.toString())
                }
            }
        })

        vm.loadUpcoming().observe(requireActivity(), { res ->
            when (res.status) {
                Status.LOADING -> {
                    if (upcomingMovieList.isNullOrEmpty())
                        upcomingSkeleton.showSkeleton()
                }
                Status.SUCCESS -> {
                    upcomingSkeleton.showOriginal()
                    upcomingMovieList.clear()
                    upcomingMovieList.addAll(res.data!!.results)
                    upcomingAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    showToast(res.msg.toString())
                }
            }
        })

        vm.loadPopular().observe(requireActivity(), { res ->
            when (res.status) {
                Status.LOADING -> {
                    if (popularMovieList.isNullOrEmpty())
                        popularSkeleton.showSkeleton()
                }
                Status.SUCCESS -> {
                    popularSkeleton.showOriginal()
                    popularMovieList.clear()
                    popularMovieList.addAll(res.data!!.results)
                    popularAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    showToast(res.msg.toString())
                }
            }
        })

        vm.loadTopRated().observe(requireActivity(), { res ->
            when (res.status) {
                Status.LOADING -> {
                    if (topRatedMovieList.isNullOrEmpty())
                        topRatedSkeleton.showSkeleton()
                }
                Status.SUCCESS -> {
                    topRatedSkeleton.showOriginal()
                    topRatedMovieList.clear()
                    topRatedMovieList.addAll(res.data!!.results)
                    topRatedAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    showToast(res.msg.toString())
                }
            }
        })
    }

    @SuppressLint("ResourceType")
    private fun initSkeletons() {
        viewPagerSkeleton = binding.homeViewPager.applySkeleton(R.layout.fragment_home_view_pager)

        upcomingSkeleton = binding.recyclerViewUpcoming.applySkeleton(
            R.layout.item_movie_home,
            itemCount = 10
        )

        popularSkeleton = binding.recyclerViewPopular.applySkeleton(
            R.layout.item_movie_home,
            itemCount = 10
        )

        topRatedSkeleton = binding.recyclerViewTopRated.applySkeleton(
            R.layout.item_movie_home,
            itemCount = 10
        )
    }


    private fun initAdapters() {
        upcomingAdapter = HomeRva(requireContext(), upcomingMovieList)
        binding.recyclerViewUpcoming.adapter = upcomingAdapter

        popularAdapter = HomeRva(requireContext(), popularMovieList)
        binding.recyclerViewPopular.adapter = popularAdapter

        topRatedAdapter = HomeRva(requireContext(), topRatedMovieList)
        binding.recyclerViewTopRated.adapter = topRatedAdapter
    }


    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.home_search_button -> {
                navController.navigate(R.id.action_homeFragment_to_searchFragment2)
            }
            R.id.text_view_all_popular -> {
                val bundle = bundleOf(Constants.viewAll to Constants.Popular)
                navController.navigate(R.id.action_homeFragment_to_viewAllFragment, bundle)
            }
            R.id.text_view_all_top_rated -> {
                val bundle = bundleOf(Constants.viewAll to Constants.TopRated)
                navController.navigate(R.id.action_homeFragment_to_viewAllFragment, bundle)
            }
            R.id.text_view_all_upcoming -> {
                val bundle = bundleOf(Constants.viewAll to Constants.Upcoming)
                navController.navigate(R.id.action_homeFragment_to_viewAllFragment, bundle)
            }
            R.id.bookmarks -> {
                val bundle = bundleOf(Constants.viewAll to Constants.Bookmarks)
                navController.navigate(R.id.action_homeFragment_to_viewAllFragment, bundle)
            }

        }

    }

}