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

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
    ): MovieResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"

        fun getImageURLFor(posterPath: String?): String {
            if (posterPath == null) return "placeholder_image_link"

            return "https://image.tmdb.org/t/p/w500${posterPath}"
        }
    }
}