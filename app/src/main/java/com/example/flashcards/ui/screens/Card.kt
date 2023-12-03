package com.example.flashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.ui.components.CardContent
import com.example.flashcards.ui.components.CardFace
import com.example.flashcards.ui.components.FlashCard
import com.example.flashcards.ui.components.IconButton
import com.example.flashcards.ui.viewmodels.TopicViewModel

// CardScreen is a composable that displays a cards for a topic.
// It uses the TopicViewModel to get the cards for the topic.
@Composable
fun CardScreen(
    topicId: Int,
) {
    val topicViewModel = viewModel(modelClass = TopicViewModel::class.java)

    // calls once when topicId changes
    LaunchedEffect(topicId) {
        topicViewModel.onTopicSelect(topicId)
    }

    val topicState = topicViewModel.state.value

    val cardCount = topicState.cards.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 8.dp,
                bottom = 32.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        var cardFace by remember {
            mutableStateOf(CardFace.Front)
        }

        // Card count
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = 24.dp,
                    bottom = 16.dp
                ),
            text = stringResource(
                R.string.card_count_last,
                topicState.currentCardInd + 1,
                cardCount
            ),
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        FlashCard(
            cardFace = cardFace,
            onClick = {
                cardFace = cardFace.next
            },
            front = {
                topicState.currentCard?.content?.let { content ->
                    CardContent(content)
                }
            },
            back = {
                topicState.currentCard?.description?.let { description ->
                    CardContent(description)
                }
            },
            modifier = Modifier
                .padding(
                    top = 48.dp,
                )
                .fillMaxSize()
        )

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {

            IconButton(
                icon = Icons.Default.KeyboardArrowLeft,
                onClick = {
                    cardFace = CardFace.Front
                    topicViewModel.previousCard()
                },
                contentDescription = stringResource(R.string.action_previous_card)
            )

            IconButton(
                icon = Icons.Default.KeyboardArrowRight,
                onClick = {
                    cardFace = CardFace.Front
                    topicViewModel.nextCard()
                },
                contentDescription = stringResource(R.string.action_next_card)
            )
        }
    }
}

