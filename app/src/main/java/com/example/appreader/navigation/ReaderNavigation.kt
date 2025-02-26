package com.example.appreader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appreader.screens.home.ReaderHomeScreen
import com.example.appreader.screens.login.ReaderLoginScreen
import com.example.appreader.screens.splash.ReaderSplashScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        composable(route = ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(route = ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }


        composable(route = ReaderScreens.HomeScreen.name) {
            ReaderHomeScreen(navController = navController)
        }
    }
}