package com.shabinder.tmdb.repo

import com.shabinder.tmdb.api.TMDBApiService
import com.shabinder.tmdb.models.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(private val apiService: TMDBApiService) {
    suspend fun getTrendingMovies(): List<Movie> {
        return apiService.getTrendingMovies().results
    }
}