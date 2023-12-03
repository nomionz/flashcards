package com.example.flashcards.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flashcards.R

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

// FlashCard is a composable that displays a card with a front and back face with a flip animation.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        ),
        label = stringResource(R.string.label_flip_anim)
    )

    Card(
        onClick = { onClick(cardFace) },
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
    ) {
        if (rotation.value <= 90f) {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                front()
            }
        } else {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f
                    },
            ) {
                back()
            }
        }
    }
}

// CardContent is a composable that holds and displays the content of a card.
@Composable
fun CardContent(
    value: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = value,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 16.dp)
        )
    }
}