package sathya.com.movieblipp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sathya.com.movieblipp.R
import sathya.com.movieblipp.data.model.Movie
import sathya.com.movieblipp.data.model.Status
import sathya.com.movieblipp.databinding.FragmentSearchBinding
import sathya.com.movieblipp.ui.adapters.SearchRva
import sathya.com.movieblipp.utils.Constants
import sathya.com.movieblipp.utils.showToast

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val vm: SearchVm by viewModels()
    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchAdapter: SearchRva
    private var searchResult: ArrayList<Movie> = ArrayList()
    private var query = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchAdapter = SearchRva()
        binding.searchRecyclerView.adapter = searchAdapter

        binding.buttonBack.setOnClickListener {
            it.findNavController().navigateUp()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                getSearchResult()
            }

        })

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getSearchResult()
                true
            }
            false
        }


    }

    fun getSearchResult() {
        if (!binding.searchEditText.text.isNullOrEmpty())
            vm.getSearchMovie(binding.searchEditText.text.toString())
                .observe(viewLifecycleOwner, Observer {
                    searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                    Log.i(Constants.TAG, it.toString())
                })
    }

    fun performSearch() {

        if (!binding.searchEditText.text.isNullOrEmpty())
            vm.searchMovie(binding.searchEditText.text.toString())
                .observe(requireActivity(), Observer { res ->

                    when (res.status) {
                        Status.LOADING -> {
                            showToast("Loading")
                        }
                        Status.SUCCESS -> {
                            searchResult.clear()
                            if (res.data != null && res.data.value != null)
                                searchAdapter.submitData(
                                    viewLifecycleOwner.lifecycle,
                                    res.data.value!!
                                )

                        }
                        Status.ERROR -> {
                        }
                    }

                })

    }


}