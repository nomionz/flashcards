package com.example.flashcards.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.flashcards.data.room.models.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Upsert
    suspend fun upsert(card: Card): Long

    @Delete
    suspend fun delete(card: Card)

    @Query("SELECT * FROM cards WHERE topic_id = :id")
    fun listByTopicId(id: Long): Flow<List<Card>>
}