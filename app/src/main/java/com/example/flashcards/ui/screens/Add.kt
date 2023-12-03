package com.example.flashcards.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcards.R
import com.example.flashcards.ui.ShowSnack
import com.example.flashcards.ui.components.CardContent
import com.example.flashcards.ui.components.CardFace
import com.example.flashcards.ui.components.FlashCard
import com.example.flashcards.ui.components.IconButton
import com.example.flashcards.ui.components.InfoDialog
import com.example.flashcards.ui.viewmodels.TopicCreateViewModel
import kotlinx.coroutines.delay

// AddScreen is a composable that displays a screen for adding a topic and cards.
@Composable
fun AddScreen(
    onNavigate: () -> Unit = {},
) {
    val topicCreateViewModel = viewModel(modelClass = TopicCreateViewModel::class.java)

    val topicState = topicCreateViewModel.state.value

    var showSnack by remember {
        mutableStateOf(false)
    }

    var snackMsg by remember {
        mutableIntStateOf(0)
    }

    // show snack bar for 2 seconds
    LaunchedEffect(key1 = showSnack) {
        if (showSnack) {
            delay(2000)
            showSnack = false
            snackMsg = 0
        }
    }

    var showCardDialog by remember {
        mutableStateOf(false)
    }

    var showInfoDialog by remember {
        mutableStateOf(false)
    }

    var cardFace by remember {
        mutableStateOf(CardFace.Front)
    }

    var cardCount by remember {
        mutableIntStateOf(0)
    }

    val focusManager = LocalFocusManager.current // used to add a Done button to the keyboard

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 32.dp,
                bottom = 32.dp
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
        ) {

            // topic name text field
            TextField(
                value = topicState.topicName,
                onValueChange = {
                    topicCreateViewModel.onTopicNameChange(it)
                },
                label = {
                    Text(text = stringResource(R.string.topic_name))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }), // adds a Done button to the keyboard
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.card_count, cardCount),
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Left
            )

            FlashCard(
                cardFace = cardFace,
                onClick = {
                    cardFace = cardFace.next
                },
                front = {
                    CardContent(
                        value = topicState.cardQuestion,
                    )
                },
                back = {
                    CardContent(
                        value = topicState.cardDescription,
                    )
                },
                modifier = Modifier
                    .height(400.dp)
            )

            TextButton(
                onClick = {
                    showCardDialog = true
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 8.dp,
                        bottom = 16.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.action_add_card_content),
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        if (showInfoDialog) {
            InfoDialog(
                onDismiss = {
                    showInfoDialog = false
                },
                message = stringResource(R.string.add_info).trimIndent()
            )
        }

        if (showCardDialog) {
            CardDialog(
                viewModel = topicCreateViewModel,
                onDismiss = {
                    showCardDialog = false
                }
            )
        }

        if (showSnack) {
            ShowSnack(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                message = snackMsg,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            IconButton(
                modifier = Modifier
                    .padding(16.dp),
                onClick = {
                    if (topicState.topicName.isEmpty()) {
                        showSnack = true
                        snackMsg = R.string.topic_name_empty
                        return@IconButton
                    }

                    if (topicCreateViewModel.isCardsEmpty()) {
                        showSnack = true
                        snackMsg = R.string.add_one_card
                        return@IconButton
                    }

                    topicCreateViewModel.saveTopic()
                    onNavigate()
                },
                icon = Icons.Default.Done,
                contentDescription = stringResource(R.string.action_save_topic)
            )

            // help/info icon button
            IconButton(
                modifier = Modifier
                    .padding(16.dp),
                icon = Icons.Default.Info,
                onClick = {
                    showInfoDialog = true
                },
                contentDescription = stringResource(R.string.add_info)
            )

            IconButton(
                modifier = Modifier
                    .padding(16.dp),
                onClick = {
                    if (topicState.cardQuestion.isEmpty()) {
                        showSnack = true
                        snackMsg = R.string.card_topic_empty
                        return@IconButton
                    }

                    if (topicState.cardDescription.isEmpty()) {
                        showSnack = true
                        snackMsg = R.string.card_description_empty
                        return@IconButton
                    }

                    // Add card to topic and reset card fields preview.
                    topicCreateViewModel.addCard()
                    topicCreateViewModel.onCardQuestionChange("")
                    topicCreateViewModel.onCardDescriptionChange("")
                    cardFace = CardFace.Front
                    cardCount += 1
                },
                icon = Icons.Default.Add,
                contentDescription = stringResource(R.string.action_add_card)
            )
        }
    }
}

// CardDialog is a composable that displays a dialog for adding a card.
@Composable
private fun CardDialog(
    viewModel: TopicCreateViewModel,
    onDismiss: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.3f)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    modifier = Modifier
                        .padding(16.dp),
                    value = viewModel.state.value.cardQuestion,
                    onValueChange = {
                        viewModel.onCardQuestionChange(it)
                    },
                    label = {
                        Text(stringResource(R.string.card_topic))
                    },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )

                TextField(
                    modifier = Modifier
                        .padding(16.dp),
                    value = viewModel.state.value.cardDescription,
                    onValueChange = {
                        viewModel.onCardDescriptionChange(it)
                    },
                    label = {
                        Text(stringResource(R.string.card_description))
                    },
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(stringResource(R.string.action_confirm))
                }
            }
        }
    }
}
