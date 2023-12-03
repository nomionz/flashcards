package com.example.flashcards.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcards.DbSingleton
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.data.room.models.TopicWithCards
import com.example.flashcards.ui.repository.FlashCardsRepository
import kotlinx.coroutines.launch

data class TopicCreateState(
    val topicName: String = "",
    val cardQuestion: String = "",
    val cardDescription: String = "",
)

// TopicCreateViewModel is a ViewModel class that provides data to the AddScreen composable.
class TopicCreateViewModel(
    private val repository: FlashCardsRepository = DbSingleton.repository
) : ViewModel() {

    var state = mutableStateOf(TopicCreateState())
        private set

    private val cards = mutableListOf<Card>()

    fun isCardsEmpty() = cards.isEmpty()

    // saveTopic saves the topic and its cards to the database.
    fun saveTopic() {
        if (cards.isEmpty()) return

        viewModelScope.launch {
            val topic = TopicWithCards(
                topic = Topic(name = state.value.topicName),
                cards = cards
            )

            repository.insertWithCards(topic)
        }
    }

    fun addCard() {
        if (state.value.cardQuestion.isEmpty() ||
            state.value.cardDescription.isEmpty()
        ) return

        val card = Card(
            content = state.value.cardQuestion,
            description = state.value.cardDescription
        )

        cards.add(card)
    }

    fun onTopicNameChange(topicName: String) {
        state.value = state.value.copy(topicName = topicName)
    }

    fun onCardQuestionChange(cardQuestion: String) {
        state.value = state.value.copy(cardQuestion = cardQuestion)
    }

    fun onCardDescriptionChange(cardDescription: String) {
        state.value = state.value.copy(cardDescription = cardDescription)
    }
}