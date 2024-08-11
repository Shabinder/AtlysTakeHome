package com.shabinder.tmdb.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun BasicErrorUI(
    message: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var moreDetailsShown by remember { mutableStateOf(false) }

        Text(
            text = "OOPS!",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text(
            text = "Something went wrong",
            style = textStyle,
        )

        Button(onClick = { moreDetailsShown = !moreDetailsShown }) {
            Text(text = if (moreDetailsShown) "Hide Details" else "Show Details")
        }

        AnimatedVisibility(moreDetailsShown) {
            Text(
                text = message,
                style = textStyle,
            )
        }
    }
}