package com.example.flashcards.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
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
import com.example.flashcards.Screen
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.ui.viewmodels.TopicViewModel

// MenuScreen is a composable that displays a list of topics.
// On click of a topic, it navigates to the CardScreen calling the onNavigate callback.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {},
) {
    val topicViewModel = viewModel(modelClass = TopicViewModel::class.java)
    val topicState = topicViewModel.state.value

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.menu_topbar))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onNavigate(Screen.AddScreen.route)
            }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_add_card)
                )
            }
        }
    ) { innerPadding -> //

        if (topicState.topics.isEmpty()) {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = stringResource(R.string.info_no_topics).trimIndent(),
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            TopicsList(
                modifier = modifier
                    .padding(innerPadding),
                topics = topicState.topics,
                // click callbacks bounded to the index of the topic
                onItemClicked = { index ->
                    onNavigate("${Screen.CardScreen.route}/${index}")
                },
                onDeleteClicked = { index ->
                    topicViewModel.deleteTopic(
                        topicState.topics[index]
                    )
                }
            )
        }
    }
}

@Composable
fun TopicsList(
    modifier: Modifier,
    topics: List<Topic> = emptyList(),
    onItemClicked: (Int) -> Unit = {},
    onDeleteClicked: (Int) -> Unit = {},
) {
    // LazyColumn is a vertically scrolling list that only composes and lays out the currently visible items.
    LazyColumn(
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        items(topics.size) { index ->
            TopicCard(
                topic = topics[index],
                onItemClicked = { onItemClicked(index) },
                onDeleteClicked = { onDeleteClicked(index) },
            )
        }
    }
}

@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    topic: Topic,
    onItemClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClicked() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = topic.name,
                modifier = modifier,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.headlineMedium,
            )

            DropDownMenu(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopEnd),
                onDeleteClicked = {
                    onDeleteClicked()
                }
            )
        }
    }
}


@Composable
private fun DropDownMenu(
    modifier: Modifier = Modifier,
    onDeleteClicked: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.action_more)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(R.string.action_delete))
                },
                onClick = {
                    onDeleteClicked()
                    expanded = false
                }
            )
        }
    }
}

