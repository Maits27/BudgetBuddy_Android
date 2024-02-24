package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SegundaPantalla(navController: NavController, appViewModel: AppViewModel, modifier: Modifier){
    Scaffold {
            NuevoGasto(navController = navController, appViewModel = appViewModel, modifier = modifier)
    }
}

@Composable
fun NuevoGasto(navController: NavController, appViewModel: AppViewModel, modifier: Modifier){

}