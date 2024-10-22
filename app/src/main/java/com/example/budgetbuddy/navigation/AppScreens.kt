package com.example.budgetbuddy.navigation

/**
 * Un objeto sellado, en Kotlin, es una estructura de datos que permite definir un conjunto fijo
 * y limitado de subtipos (clases, objetos o interfaces). Es un conjunto cerrado de opciones que
 * garantiza que ninguna otra clase pueda extender o implementar el conjunto definido.
 *
 * En este caso se utiliza para definir las pantallas de la aplicación
 */

sealed class AppScreens (val route: String) {
    object Home: AppScreens("Home")
    object Add: AppScreens("Add")
    object Edit: AppScreens("Edit")
    object Dashboards: AppScreens("Dashboards")
    object Facturas: AppScreens("Facturas")
}