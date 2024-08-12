package com.shabinder.tmdb.repo

import com.shabinder.tmdb.api.TMDBApiService
import com.shabinder.tmdb.models.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: TMDBApiService) {
    suspend fun getTrendingMovies(): List<Movie> = withContext(Dispatchers.IO) {
        apiService.getTrendingMovies().results
    }

    suspend fun searchMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        apiService.searchMovies(query).results
    }
}