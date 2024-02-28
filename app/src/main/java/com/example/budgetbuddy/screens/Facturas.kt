package com.example.budgetbuddy2.screens

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Calendario
import com.example.budgetbuddy.R
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Facturas(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    var fecha by rememberSaveable { mutableStateOf(appViewModel.fecha) }
    var showCalendar by remember { mutableStateOf(false) }

    var factura = stringResource(id = R.string.factura_init)
    factura += crearElementosFactura(appViewModel)
    factura += stringResource(id = R.string.factura_total, appViewModel.gastoTotal().toString())
    appViewModel.cambiarFactura(factura)
    Column (
        modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = stringResource(id = R.string.date, "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year}"))
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
            Text(
                text = factura,
                Modifier
                    .padding(16.dp)
                    .background(color = Color.Transparent),
                color = Color.DarkGray)
        }
        Button(onClick = { showCalendar = true }) {
            Text(text = stringResource(id = R.string.date_pick))
        }
        Calendario(show = showCalendar, appViewModel = appViewModel) {
            fecha = it
            showCalendar = false
            appViewModel.cambiarFecha(fecha)
        }
    }
}

private fun crearElementosFactura(appViewModel: AppViewModel): String{
    var factura = ""
    for (gasto in appViewModel.listadoGastos){
        factura += "${gasto.nombre}:\t${gasto.cantidad}€\n"
    }
    return factura+"\n"
}


