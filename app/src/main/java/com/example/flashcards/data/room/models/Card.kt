package com.example.flashcards.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "topic_id", index = true)
    val topicId: Long = 0,
    val content: String,
    val description: String,
)