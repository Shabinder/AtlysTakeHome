package com.shabinder.tmdb.ui

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shabinder.tmdb.models.Movie
import com.shabinder.tmdb.repo.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@Stable
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MovieResult>(MovieResult.Loading)
    val state: StateFlow<MovieResult> = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _searchState = MutableStateFlow<MovieResult>(MovieResult.Loading)
    val searchState: StateFlow<MovieResult> = _searchState.asStateFlow()

    init {
        fetchTrendingMovies()

        viewModelScope.launch {
            query.debounce(300)
                .distinctUntilChanged()
                .collectLatest {
                    if (it.isNotEmpty()) {
                        searchMovies(it)
                    } else {
                        _searchState.value = MovieResult.Loading
                    }
                }
        }
    }

    private fun fetchTrendingMovies() {
        _state.value = MovieResult.Loading
        viewModelScope.launch {
            try {
                val movies = repository.getTrendingMovies()
                _state.value = MovieResult.Success(movies)
            } catch (e: Exception) {
                _state.value = MovieResult.Error(e)
            }
        }
    }

    private suspend fun searchMovies(query: String) {
        _searchState.value = MovieResult.Loading
        try {
            val movies = repository.searchMovies(query)
            _searchState.value = MovieResult.Success(movies)
        } catch (e: Exception) {
            _searchState.value = MovieResult.Error(e)
        }
    }

    fun getMovie(id: Int): Movie? {
        return (state.value as? MovieResult.Success)?.movies?.find { it.id == id }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    sealed interface MovieResult {
        data object Loading : MovieResult
        data class Success(val movies: List<Movie>) : MovieResult
        data class Error(val e: Throwable) : MovieResult
    }
}