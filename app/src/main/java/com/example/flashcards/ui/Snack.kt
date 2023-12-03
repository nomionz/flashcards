package com.example.flashcards.ui

import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun ShowSnack(
    modifier: Modifier = Modifier,
    message: Int,
) {
    Snackbar(
        modifier = modifier,
    ) {
        Text(text = stringResource(message))
    }
}