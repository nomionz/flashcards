package com.example.flashcards.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic
import com.example.flashcards.data.room.models.TopicWithCards
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    @Query("SELECT * FROM topics ORDER BY id ASC")
    fun list(): Flow<List<Topic>>

    @Query("SELECT * FROM topics WHERE id = :id")
    suspend fun get(id: Long): Topic

    @Upsert
    suspend fun upsert(topic: Topic): Long

    @Delete
    suspend fun delete(topic: Topic)

    @Insert
    suspend fun insertCards(cards: List<Card>)

    @Transaction
    suspend fun insertWithCards(topic: TopicWithCards) {
        val topicId = upsert(topic.topic)

        val cardsWithTopicId = topic.cards.map { card ->
            card.copy(topicId = topicId)
        }

        insertCards(cardsWithTopicId)
    }
}