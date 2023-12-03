package com.example.flashcards.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

// IconButton is a composable that displays a floating action button with an icon.
@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        modifier = modifier
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}