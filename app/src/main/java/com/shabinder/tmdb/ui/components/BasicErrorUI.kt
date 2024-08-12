package com.shabinder.tmdb.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        var moreDetailsShown by remember { mutableStateOf(false) }

        if (content != null) {
            content()
            Spacer(modifier = Modifier.height(16.dp))
        }

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