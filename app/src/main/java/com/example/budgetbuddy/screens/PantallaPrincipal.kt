package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
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
        Column (
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Text(text = stringResource(id = R.string.list_explain))
            Divider()
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ){
                items(appViewModel.listadoGastos){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.wrapContentSize()
                    ){
                        Card (
                            modifier = Modifier.fillMaxWidth(),
                            shape = CardDefaults.elevatedShape
                        ){
                            Text(text = it.nombre)
                            Text(text = stringResource(id = R.string.cantidad, it.cantidad))
                        }
                    }
                }
            }
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