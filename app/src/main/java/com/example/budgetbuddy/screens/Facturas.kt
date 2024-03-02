package com.example.budgetbuddy2.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.screens.Calendario
import com.example.budgetbuddy.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Facturas(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    val coroutineScope = rememberCoroutineScope()

    var fecha by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var showCalendar by remember { mutableStateOf(false) }

    var factura by remember { mutableStateOf("") }
    var empty by remember { mutableStateOf(false)  }

    coroutineScope.launch(Dispatchers.IO) {
        fecha = appViewModel.fecha
        factura = appViewModel.cambiarFactura()
        empty = appViewModel.fechaisEmpty()
    }

    Column (
        modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = stringResource(id = R.string.date, appViewModel.escribirFecha(fecha)),
            modifier = Modifier.padding(16.dp))
        if (!empty){
            Card (
                shape = CardDefaults.outlinedShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(pressedElevation = 4.dp),
                border = BorderStroke(width = 2.dp, color = Color.DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.factura_init, appViewModel.facturaActual))
                Text(
                    text = factura,
                    Modifier
                        .padding(16.dp)
                        .background(color = Color.Transparent),
                    color = Color.DarkGray)
                Text(text = stringResource(id = R.string.factura_total, appViewModel.totalGasto))
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
        Button(
            onClick = { showCalendar = true },
            Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.date_pick))
        }
        Calendario(show = showCalendar, appViewModel = appViewModel) {
            fecha = it
            showCalendar = false
            appViewModel.cambiarFecha(fecha)
        }
    }
}



