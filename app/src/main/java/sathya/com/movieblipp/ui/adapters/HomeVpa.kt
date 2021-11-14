package sathya.com.movieblipp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.ui.home.HomeVpFragment

class HomeVpa(
    fm: FragmentManager, lifecycle: Lifecycle,
    val movies: List<Movie> = ArrayList()
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = movies.size

    override fun createFragment(position: Int): Fragment {
        return HomeVpFragment(movies[position])
    }

}