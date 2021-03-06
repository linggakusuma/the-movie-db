package com.lingga.themoviedb.ui.movie

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.Resource
import com.domain.model.Movie
import com.lingga.themoviedb.R
import com.lingga.themoviedb.databinding.FragmentMovieBinding
import com.lingga.themoviedb.ui.ViewModelFactory
import com.lingga.themoviedb.ui.base.BaseFragment
import com.lingga.themoviedb.utils.ext.hide
import com.lingga.themoviedb.utils.ext.observe
import com.lingga.themoviedb.utils.ext.show
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
class MovieFragment : BaseFragment<FragmentMovieBinding>(R.layout.fragment_movie) {

    @Inject
    lateinit var factory: ViewModelFactory

    private val viewModel: MovieViewModel by viewModels { factory }

    private val adapter by lazy { MovieHomeAdapter { navigateToDetail(it) } }

    private val adapterNowPlaying by lazy { MovieNowPlayingAdapter { navigateToDetail(it) } }

    private val adapterUpcoming by lazy { MovieUpcomingAdapter { navigateToDetail(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding()
        subscribeUi()
    }

    private fun initBinding() {
        binding.apply {
            appbar.apply {
                textTitle.text = getString(R.string.movie)
                buttonSetting.setOnClickListener { navigateToSetting() }
            }
            recyclerViewMovie.apply {
                adapter = this@MovieFragment.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            recyclerViewNowPlayingMovie.apply {
                adapter = this@MovieFragment.adapterNowPlaying
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            recyclerViewUpcomingMovie.apply {
                adapter = this@MovieFragment.adapterUpcoming
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            seeMorePopularMovie.setOnClickListener { navigateToPopularMovie() }
            seeMoreNowPlayingMovie.setOnClickListener { navigateToNowPlayingMovie() }
            seeMoreUpcomingMovie.setOnClickListener { navigateToUpcomingMovie() }
            searchMovie(this)
        }
    }

    private fun subscribeUi() {
        observe(viewModel.movie("popular") ?: return) { movie ->
            Log.d("cekmovie", movie.data.toString())
            binding.apply {
                when (movie) {
                    is Resource.Loading -> isLoading(this)
                    is Resource.Success -> {
                        isSuccess(this)
                        adapter.submitList(movie.data ?: return@apply)
                    }
                    is Resource.Error -> {
                        loading.progressBar.hide()
                        if (movie.data.isNullOrEmpty()) {
                            viewError.errorContainer.show()
                            viewError.errorMessage.text =
                                movie.message ?: getString(R.string.oopss_something_went_wrong)
                        } else {
                            isSuccess(this)
                            adapter.submitList(movie.data ?: return@apply)
                        }
                    }
                }
            }
        }
        observe(viewModel.nowPlayingMovie("nowplaying") ?: return) { movie ->
            Log.d("cekmovienowplay", movie.data.toString())
            binding.apply {
                when (movie) {
                    is Resource.Loading -> isLoading(this)
                    is Resource.Success -> {
                        isSuccess(this)
                        adapterNowPlaying.submitList(movie.data ?: return@apply)
                    }
                    is Resource.Error -> {
                        loading.progressBar.hide()
                        if (movie.data.isNullOrEmpty()) {
                            viewError.errorContainer.hide()
                            viewError.errorMessage.text =
                                movie.message ?: getString(R.string.oopss_something_went_wrong)
                        } else {
                            isSuccess(this)
                            adapterNowPlaying.submitList(movie.data ?: return@apply)
                        }
                    }
                }
            }
        }
        observe(viewModel.upComingMovie("upcoming") ?: return) { movie ->
            Log.d("cekupcoming", movie.data.toString())
            binding.apply {
                when (movie) {
                    is Resource.Loading -> isLoading(this)
                    is Resource.Success -> {
                        isSuccess(this)
                        adapterUpcoming.submitList(movie.data ?: return@apply)
                    }
                    is Resource.Error -> {
                        loading.progressBar.hide()
                        if (movie.data.isNullOrEmpty()) {
                            viewError.errorContainer.hide()
                            viewError.errorMessage.text =
                                movie.message ?: getString(R.string.oopss_something_went_wrong)
                        } else {
                            isSuccess(this)
                            adapterUpcoming.submitList(movie.data ?: return@apply)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToDetail(movie: Movie) {
        findNavController().navigate(
            MovieFragmentDirections.actionMovieFragmentToDetailFragment(movie)
        )
    }

    private fun navigateToSetting() {
        findNavController().navigate(MovieFragmentDirections.actionMovieFragmentToSettingFragment())
    }

    private fun navigateToPopularMovie() {
        findNavController().navigate(MovieFragmentDirections.actionMovieFragmentToMoviePopularFragment())
    }

    private fun navigateToNowPlayingMovie() {
        findNavController().navigate(MovieFragmentDirections.actionMovieFragmentToMovieNowPlayingFragment())
    }

    private fun navigateToUpcomingMovie() {
        findNavController().navigate(MovieFragmentDirections.actionMovieFragmentToMovieUpcomingFragment())
    }

    private fun isLoading(binding: FragmentMovieBinding) {
        binding.apply {
            loading.progressBar.show()
            labelMoviePopular.hide()
            labelNowPlayingMovie.hide()
            seeMoreNowPlayingMovie.hide()
            seeMoreUpcomingMovie.hide()
            seeMorePopularMovie.hide()
            labelUpcomingMovie.hide()
        }
    }

    private fun isSuccess(binding: FragmentMovieBinding) {
        binding.apply {
            loading.progressBar.hide()
            labelMoviePopular.show()
            labelNowPlayingMovie.show()
            labelUpcomingMovie.show()
            seeMoreNowPlayingMovie.show()
            seeMorePopularMovie.show()
            seeMoreUpcomingMovie.show()
        }
    }

    private fun searchMovie(binding: FragmentMovieBinding) {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                findNavController().navigate(
                    MovieFragmentDirections.actionMovieFragmentToSearchMovieFragment(
                        query
                    )
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }
}