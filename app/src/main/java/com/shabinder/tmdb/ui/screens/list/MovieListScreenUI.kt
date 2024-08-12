package com.shabinder.tmdb.ui.screens.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.shabinder.tmdb.api.TMDBApiService
import com.shabinder.tmdb.models.Movie
import com.shabinder.tmdb.ui.MovieViewModel
import com.shabinder.tmdb.ui.components.BasicErrorUI
import com.shabinder.tmdb.ui.components.BasicLoaderUI
import com.shabinder.tmdb.ui.MovieViewModel.MovieResult

@Composable
fun MovieListScreenUI(
    viewModel: MovieViewModel,
    modifier: Modifier = Modifier,
    onMovieClick: (Movie) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    val isSearching by remember {
        derivedStateOf {
            query.isNotEmpty()
        }
    }

    MovieListScreenUI(
        isSearching = isSearching,
        query = query,
        state = state,
        searchState = searchState,
        onQueryChange = viewModel::updateQuery,
        onMovieClick = onMovieClick,
        modifier = modifier,
    )
}

@Composable // Without ViewModel, for Easier Preview
fun MovieListScreenUI(
    isSearching: Boolean,
    query: String,
    state: MovieResult,
    searchState: MovieResult,
    onQueryChange: (String) -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier
) {
    Column(modifier) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            isSearching = isSearching,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        AnimatedContent(
            targetState = if (isSearching) searchState else state,
            label = "MovieScreenContent",
            modifier = Modifier.fillMaxSize(),
        ) { s ->
            when (s) {
                is MovieResult.Loading -> {
                    BasicLoaderUI(modifier = Modifier.fillMaxSize())
                }

                is MovieResult.Success -> {
                    MovieGridList(
                        movies = s.movies,
                        onMovieClick = onMovieClick,
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
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    isSearching: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        placeholder = { Text(text = "Search Movies") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = isSearching,
                enter = fadeIn() + slideInHorizontally { it / 2 },
                exit = fadeOut() + slideOutHorizontally { it / 2 },
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear Search")
                }
            }
        },
        modifier = modifier
    )
}

@Composable
private fun MovieGridList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = movies,
            key = { movie -> movie.id },
        ) { movie ->
            MovieItem(
                movie = movie,
                modifier = Modifier.clickable { onMovieClick(movie) }
            )
        }
    }
}

@Composable
private fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = TMDBApiService.getImageURLFor(movie.posterPath)
            ),
            contentDescription = "Movie Poster",
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )

        Text(
            text = movie.title,
            textAlign = TextAlign.Start,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListScreenUIPreview() {
    MovieListScreenUI(
        query = "",
        isSearching = false,
        state = MovieResult.Success(
            List(10) {
                Movie(
                    id = it,
                    title = "Movie Name $it",
                    overview = "Overview of Movie $it",
                    posterPath = "/poster.jpg",
                )
            }
        ),
        searchState = MovieResult.Success(
            List(5) {
                Movie(
                    id = it,
                    title = "Search Movie $it",
                    overview = "Overview of Search Movie $it",
                    posterPath = "/search_poster.jpg",
                )
            }
        ),
        onQueryChange = {},
        onMovieClick = {},
        modifier = Modifier.fillMaxSize()
    )
}