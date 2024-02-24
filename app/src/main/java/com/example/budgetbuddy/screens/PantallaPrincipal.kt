package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.Idioma
import com.example.budgetbuddy.R
import java.util.Locale
import android.content.res.Resources
import androidx.compose.material.icons.filled.Settings

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PantallaPrincipal(navController: NavController, appViewModel: AppViewModel, modifier: Modifier){
    Scaffold {
        BodyContent(navController, appViewModel, modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyContent(navController: NavController, appViewModel: AppViewModel, modifier: Modifier=Modifier){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showInfo by rememberSaveable { mutableStateOf(false) }
    var showLang by rememberSaveable { mutableStateOf(false) }
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.tertiary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = stringResource(id = R.string.menu),
                            tint = Color.White
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                actions = {
                    IconButton( onClick = { showInfo = true } ){
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.infor),
                            tint = Color.White
                        )
                    }
                    IconButton( onClick = { showLang = true } ){
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.infor),
                            tint = Color.White
                        )
                    }
                }
            )
            Informacion(showInfo) { showInfo = false }
            Idiomas(showLang, appViewModel){ showLang = false }
        }
    ){ innerPadding ->
        ScrollContent(innerPadding, appViewModel, modifier)
    }
}
@Composable
fun ScrollContent(innerPadding: PaddingValues, appViewModel: AppViewModel, modifier: Modifier){
    Column (
        modifier = modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = stringResource(id = R.string.list_explain),
            Modifier.padding(16.dp)
        )
        Divider()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
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

@Composable
fun Informacion(show: Boolean, onConfirm: () -> Unit) {
    if(show){
        AlertDialog(
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.app_name)) },
            text = {
                Text(text = stringResource(id = R.string.app_description))
            }
        )
    }
}

@Composable
fun Idiomas(show: Boolean, appViewModel: AppViewModel, onConfirm: () -> Unit) {
    if(show){
        AlertDialog(
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }){
                Text(text = stringResource(id = R.string.ok))
            }},
            title = { Text(text = stringResource(id = R.string.change_lang)) },
            text = {
                Column (
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    for (idioma in Idioma.entries){
                        Button(
                            onClick = {
                                onConfirm()
                                appViewModel.cambiarIdioma(idioma.code)},
                            Modifier.fillMaxWidth()
                        ) {
                            Text(text = idioma.language)
                        }
                    }
                }
            }
        )
    }
}
