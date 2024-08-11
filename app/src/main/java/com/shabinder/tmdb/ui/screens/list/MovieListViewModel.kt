package com.shabinder.tmdb.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shabinder.tmdb.models.Movie
import com.shabinder.tmdb.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MovieResult>(MovieResult.Loading)
    val state: StateFlow<MovieResult> = _state.asStateFlow()

    fun fetchTrendingMovies() {
        viewModelScope.launch {
            try {
                val movies = repository.getTrendingMovies()
                _state.value = MovieResult.Success(movies)
            } catch (e: Exception) {
                _state.value = MovieResult.Error(e)
            }
        }
    }

    sealed interface MovieResult {
        data object Loading : MovieResult
        data class Success(val movies: List<Movie>) : MovieResult
        data class Error(val e: Throwable) : MovieResult
    }
}