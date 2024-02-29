package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.budgetbuddy.Data.Idioma
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Calendario
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens


@Composable
fun Home(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onEdit:(Gasto)->Unit,
){

    val forceRefresh by appViewModel.forceRefresh.collectAsState()
    // Actualiza la lista cuando forceRefresh cambia
    if (forceRefresh) {
        // Lógica para recargar la lista o realizar cualquier otra acción necesaria
        appViewModel.refreshComplete() // Marcar como completado después de actualizar
    }

    var showCalendar by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = stringResource(id = R.string.list_explain, appViewModel.escribirFecha()),
            Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = { showCalendar = true },
            Modifier.padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.date_pick))
        }
        Calendario(show = showCalendar, appViewModel = appViewModel) {
            showCalendar = false
            appViewModel.cambiarFecha(it)
        }
        Divider()
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, modifier = Modifier.padding(6.dp)
        ){
            items(appViewModel.listadoGastosFecha){
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    shape = CardDefaults.elevatedShape,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ){
                    Row (
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column (
                            modifier
                                .padding(16.dp)
                                .weight(3f)
                        ){
                            Text(text = it.nombre)
                            Text(text = stringResource(id = R.string.cantidad, it.cantidad))
                            Text(text = stringResource(id = R.string.tipo, it.tipo.tipo))
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            onClick = {
                                onEdit(it)
                                navController.navigate(AppScreens.Edit.route)
                            }
                        ) {
                            Icon(Icons.Filled.Create, stringResource(id = R.string.edit), tint = Color.Black)
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            onClick = { appViewModel.borrarGasto(it) }
                        ) {
                            Icon(Icons.Filled.Delete, stringResource(id = R.string.add), tint = Color.Black)
                        }
                    }
                }
            }
        }
    }
}


