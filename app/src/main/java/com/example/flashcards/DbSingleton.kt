package com.example.flashcards

import android.content.Context
import com.example.flashcards.data.room.FlashCardsDatabase
import com.example.flashcards.ui.repository.FlashCardsRepository

// DbSingleton is a singleton class that provides access to the database and repository
object DbSingleton {
    lateinit var db: FlashCardsDatabase
        private set

    val repository by lazy {
        FlashCardsRepository(
            topicDao = db.topicDao(),
            cardDao = db.cardDao()
        )
    }

    fun provide(context: Context) {
        db = FlashCardsDatabase.getDatabase(context)
    }
}