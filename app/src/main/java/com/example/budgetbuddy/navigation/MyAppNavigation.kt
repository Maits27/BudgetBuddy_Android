package com.example.budgetbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.screens.PantallaPrincipal
import com.example.budgetbuddy.screens.SegundaPantalla

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel,
    cambiarIdioma: (String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppScreens.PantallaPrincipal.route
    ) {
        composable(route = AppScreens.PantallaPrincipal.route){
            PantallaPrincipal(navController, appViewModel, modifier){code: String -> cambiarIdioma(code)}
        }
        composable(route = AppScreens.SegundaPantalla.route){
            SegundaPantalla(navController, appViewModel, modifier)
        }
    }
}
