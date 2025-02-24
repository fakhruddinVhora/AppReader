package com.example.appreader.navigation

enum class ReaderScreens {
    SplashScreen, LoginScreen, CreateAccountScreen, HomeScreen, StatsScreen, DetailsScreen, UpdateScreen;


    companion object {
        fun fromRoute(route: String?): ReaderScreens {
            return when (route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                CreateAccountScreen.name -> CreateAccountScreen
                HomeScreen.name -> HomeScreen
                StatsScreen.name -> StatsScreen
                DetailsScreen.name -> DetailsScreen
                UpdateScreen.name -> UpdateScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}