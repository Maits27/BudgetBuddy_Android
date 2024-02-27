package com.example.budgetbuddy.navigation


sealed class AppScreens (val route: String) {
    object Home: AppScreens("Home")
    object Add: AppScreens("Add")
    object Dashboards: AppScreens("Dashboards")
    object Facturas: AppScreens("Facturas")
}