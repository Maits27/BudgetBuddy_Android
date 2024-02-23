package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaPrincipal(navController: NavController, appViewModel: AppViewModel){
    Scaffold {
        BodyContent(navController, appViewModel)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BodyContent(navController: NavController, appViewModel: AppViewModel, modifier: Modifier=Modifier){
    Scaffold (
        topBar = {ParteArriba()},
        bottomBar = {ParteAbajo(appViewModel)}
    ){
        Column {
        /*  TODO:
             Uso de ListView+CardView personalizado o de RecyclerView+CardView
             para mostrar listados de elementos con diferentes características.
             Usar una base de datos local, para listar, añadir y modificar elementos
             y características de cada elemento.
             Uso de diálogos.
             Usar notificaciones locales
             Permitir que una misma funcionalidad se comporte de manera distinta
             dependiendo de la orientación (o del tamaño) del dispositivo mediante
             el uso de Fragments (1 punto).
        * */
        }
    }
}

@Composable
fun ParteArriba(modifier: Modifier=Modifier){
    Column {
        Text(text = stringResource(id = R.string.app_name))
    }
}
@Composable
fun ParteAbajo(appViewModel: AppViewModel, modifier: Modifier=Modifier){
 //TODO Añadir una barra de acciones (ActionBar) personalizada en la aplicación (1 punto).
}