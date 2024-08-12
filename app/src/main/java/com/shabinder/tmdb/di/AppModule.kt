package com.shabinder.tmdb.di

import com.shabinder.tmdb.api.TMDBApiService
import com.shabinder.tmdb.repo.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(json: Json): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TMDBApiService.BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=UTF8".toMediaType())
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideTMDBApiService(retrofit: Retrofit): TMDBApiService {
        return retrofit.create(TMDBApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(apiService: TMDBApiService): MovieRepository {
        return MovieRepository(apiService)
    }
}