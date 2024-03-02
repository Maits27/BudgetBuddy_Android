package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onEdit:(Gasto)->Unit,
){
    val coroutineScope = rememberCoroutineScope()


    var showCalendar by remember { mutableStateOf(false) }
    var empty by remember { mutableStateOf(true) }

    var gastos: List<Gasto> = listOf()
    coroutineScope.launch(Dispatchers.IO) {
        empty = appViewModel.fechaisEmpty()
        gastos = appViewModel.listadoGastos.collectAsState(initial = emptyList())
    }

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
        if(!empty){
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center, modifier = Modifier.padding(6.dp)
            ){
                gastos.forEach{

                }
                items(gastos){
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
                                    navController.navigate(AppScreens.Edit.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
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
        }else{
            Column (
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 10.dp)
                    .height(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(text = stringResource(id = R.string.no_data))
            }
        }
    }
}


