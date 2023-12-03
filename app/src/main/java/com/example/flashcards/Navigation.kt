package com.example.flashcards

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flashcards.ui.screens.AddScreen
import com.example.flashcards.ui.screens.CardScreen
import com.example.flashcards.ui.screens.MenuScreen

// Navigation is a composable that defines the navigation graph for the app.
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MenuScreen.route
    ) {
        // MenuScreen is the start destination.
        composable(route = Screen.MenuScreen.route) {
            MenuScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        // Will be launched when the user clicks on a topic in the MenuScreen.
        composable(
            route = Screen.CardScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) {
            // launch CardScreen with topicId. If for some reason the id is null, launch with 0
            CardScreen(topicId = it.arguments?.getInt("id") ?: 0)
        }

        // Will be launched when the user clicks on the add button in the MenuScreen.
        composable(route = Screen.AddScreen.route) {
            AddScreen(
                onNavigate = {
                    navController.popBackStack() // go back to the MenuScreen
                }
            )
        }
    }
}