package com.example.flashcards.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.flashcards.data.room.dao.CardDao
import com.example.flashcards.data.room.dao.TopicDao
import com.example.flashcards.data.room.models.Card
import com.example.flashcards.data.room.models.Topic

// FlashCardsDatabase is a Room database that contains the Topic and Card tables
@Database(
    entities = [Topic::class, Card::class],
    version = 1,
    exportSchema = false
)
abstract class FlashCardsDatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: FlashCardsDatabase? = null
        
        fun getDatabase(context: Context): FlashCardsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FlashCardsDatabase::class.java,
                    "topic_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}