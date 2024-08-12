package com.shabinder.tmdb.ui

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoutes {

    @Serializable
    data object Trending : NavigationRoutes

    @Serializable
    data class MovieDetail(val movieID: Int) : NavigationRoutes
}