package com.example.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DbSingleton.provide(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            Navigation()
        }
    }
}
