package com.example.budgetbuddy.navigation

sealed class AppScreens (val route: String) {
    object PantallaPrincipal: AppScreens("pantalla_principal")
    object SegundaPantalla: AppScreens("segunda_pantalla")

}