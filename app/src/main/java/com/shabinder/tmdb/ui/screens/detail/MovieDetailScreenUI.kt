package com.shabinder.tmdb.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.shabinder.tmdb.api.TMDBApiService
import com.shabinder.tmdb.models.Movie

@Composable
fun MovieDetailScreenUI(
    movie: Movie,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    Column(modifier) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        Image(
            painter = rememberAsyncImagePainter(
                model = TMDBApiService.getImageURLFor(movie.posterPath)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 4f)
                .clip(MaterialTheme.shapes.medium),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(movie.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(movie.overview, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailScreenUIPreview() {
    MovieDetailScreenUI(
        movie = Movie(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            posterPath = "/poster.jpg",
        ),
        onBackClick = {}
    )
}