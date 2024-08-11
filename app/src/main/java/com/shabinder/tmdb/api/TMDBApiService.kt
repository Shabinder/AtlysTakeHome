package com.shabinder.tmdb.api

import retrofit2.http.GET
import retrofit2.http.Query
import com.shabinder.tmdb.BuildConfig
import com.shabinder.tmdb.models.MovieResponse

interface TMDBApiService {
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY
    ): MovieResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}