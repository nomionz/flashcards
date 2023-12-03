package com.example.flashcards

// Screen is a sealed class that represents the screens in the app.
sealed class Screen(val route: String) {
    data object MenuScreen : Screen("menu_screen")
    data object CardScreen : Screen("card_screen")
    data object AddScreen : Screen("add_screen")
}
