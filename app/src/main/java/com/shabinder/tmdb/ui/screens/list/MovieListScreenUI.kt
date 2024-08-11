package com.shabinder.tmdb.ui.screens.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shabinder.tmdb.models.Movie
import com.shabinder.tmdb.ui.components.BasicErrorUI
import com.shabinder.tmdb.ui.components.BasicLoaderUI
import com.shabinder.tmdb.ui.screens.list.MovieListViewModel.MovieResult

@Composable
fun MovieListScreenUI(
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = state,
        label = "MovieScreenContent",
        modifier = modifier,
    ) { s ->
        when (s) {
            is MovieResult.Loading -> {
                BasicLoaderUI(modifier = Modifier.fillMaxSize())
            }

            is MovieResult.Success -> {
                MovieListUI(
                    movies = s.movies,
                    modifier = Modifier.fillMaxSize(),
                    onMovieClick = { /* Handle movie click */ },
                )
            }

            is MovieResult.Error -> {
                BasicErrorUI(
                    message = s.e.message ?: "Unknown error",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun MovieListUI(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    onMovieClick: (Movie) -> Unit,
) {
    var query by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier) {
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = movies.take(20),
                key = { movie -> movie.id },
            ) { movie ->
                MovieItem(movie = movie, onMovieClick = onMovieClick)
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Show movie item
}