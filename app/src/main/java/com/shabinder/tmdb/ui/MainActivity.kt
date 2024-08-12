package com.shabinder.tmdb.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.shabinder.tmdb.models.Movie
import com.shabinder.tmdb.ui.NavigationRoutes.*
import com.shabinder.tmdb.ui.components.BasicErrorUI
import com.shabinder.tmdb.ui.screens.detail.MovieDetailScreenUI
import com.shabinder.tmdb.ui.screens.list.MovieListScreenUI
import com.shabinder.tmdb.ui.theme.TmdbTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TmdbTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TMDBApp(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TMDBApp(
    viewModel: MovieViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Trending,
        modifier = modifier
    ) {
        composable<Trending> {
            MovieListScreenUI(
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize(),
                onMovieClick = { movie ->
                    navController.navigate(MovieDetail(movie.id))
                }
            )
        }

        composable<MovieDetail> { backStackEntry ->
            val details: MovieDetail = backStackEntry.toRoute()
            val movie = viewModel.getMovie(details.movieID)

            if (movie != null) {
                val movieModel = rememberSaveable(saver = Movie.SAVER) { movie }
                MovieDetailScreenUI(
                    movie = movieModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    onBackClick = navController::popBackStack
                )
            } else {
                BasicErrorUI(
                    message = "Movie not found",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    IconButton(onClick = navController::popBackStack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExamplePreview() {
    TmdbTheme {
        Text("Android")
    }
}