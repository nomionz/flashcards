package com.example.flashcards.data.room.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "topics")
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
)

// TopicWithCards is a data class to describe one-to-one relationship between Card and Topic
data class TopicWithCards(
    @Embedded val topic: Topic,
    @Relation(
        parentColumn = "id",
        entityColumn = "topic_id"
    )
    val cards: List<Card>,
)

