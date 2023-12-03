package com.example.flashcards.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.DbSingleton
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.ui.repository.FlashCardsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TopicState is a data class that holds the state of the TopicViewModel
data class TopicState(
    val topics: List<Topic> = emptyList(),
    val cards: List<Card> = emptyList(),
    val currentCardInd: Int = 0, // for navigating through cards
    val currentCard: Card? = null, // for displaying the current card
)

// TopicViewModel is a ViewModel that is used by UI components to interact with the repository
class TopicViewModel(
    private val repository: FlashCardsRepository = DbSingleton.repository
) : ViewModel() {

    var state = mutableStateOf(TopicState())
        private set

    init {
        getTopics()
    }

    private fun getTopics() {
        viewModelScope.launch {
            repository.topics.collectLatest { topics ->
                state.value = state.value.copy(topics = topics)
            }
        }
    }

    fun deleteTopic(topic: Topic) {
        viewModelScope.launch {
            repository.deleteTopic(topic)
        }
    }

    fun onTopicSelect(topicInd: Int) {
        if (topicInd >= state.value.topics.size) return

        if (topicInd < 0) return

        try {
            getCardsByTopicId(state.value.topics[topicInd].id)
        } catch (e: Exception) {
            Log.d("TopicViewModel", "onTopicSelect: ${e.message}")
        }
    }

    private fun getCardsByTopicId(topicId: Long) {
        viewModelScope.launch {
            repository.getCardsByTopicId(topicId).collectLatest { cards ->
                val shuffled = cards.shuffled(
                    java.util.Random(System.currentTimeMillis())
                )

                state.value = state.value.copy(
                    cards = shuffled,
                    currentCard = shuffled[state.value.currentCardInd],
                )
            }
        }
    }

    fun nextCard() {
        if (state.value.currentCardInd + 1 >= state.value.cards.size) return

        state.value = state.value.copy(
            currentCardInd = state.value.currentCardInd + 1,
            currentCard = state.value.cards[state.value.currentCardInd + 1]
        )
    }

    fun previousCard() {
        if (state.value.currentCardInd - 1 < 0) return

        state.value = state.value.copy(
            currentCardInd = state.value.currentCardInd - 1,
            currentCard = state.value.cards[state.value.currentCardInd - 1]
        )
    }
}

