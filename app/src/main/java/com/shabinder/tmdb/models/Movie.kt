package com.shabinder.tmdb.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Immutable
@Serializable
data class MovieResponse(
    val results: List<Movie>
)

@Immutable
@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String?
) {
    companion object {
        val SAVER: Saver<Movie, String> = Saver(
            save = { Json.encodeToString(serializer(), it) },
            restore = { json -> Json.decodeFromString(serializer(), json) }
        )
    }
}