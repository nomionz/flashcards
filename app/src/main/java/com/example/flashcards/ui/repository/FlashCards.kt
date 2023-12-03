package com.example.flashcards.ui.repository

import com.example.flashcards.data.room.dao.CardDao
import com.example.flashcards.data.room.dao.TopicDao
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.data.room.models.TopicWithCards

// FlashCardsRepository is a repository class that abstracts access to multiple data sources
class FlashCardsRepository(
    private val topicDao: TopicDao,
    private val cardDao: CardDao
) {
    // Topics
    val topics = topicDao.list()

    suspend fun deleteTopic(topic: Topic) = topicDao.delete(topic)

    suspend fun insertWithCards(topic: TopicWithCards) {
        topicDao.insertWithCards(topic)
    }

    // Cards
    fun getCardsByTopicId(id: Long) = cardDao.listByTopicId(id)
}