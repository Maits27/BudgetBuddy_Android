package com.example.budgetbuddy2.screens

import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.R
import java.io.File
import java.io.FileWriter
import java.io.IOException

@Composable
fun Facturas(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    var factura = stringResource(id = R.string.factura_init)
    factura += crearElementosFactura(appViewModel)
    factura += stringResource(id = R.string.factura_total, appViewModel.gastoTotal().toString())
    appViewModel.cambiarFactura(factura)
    Column (
        modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Card (
            shape = CardDefaults.outlinedShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            elevation = CardDefaults.cardElevation(pressedElevation = 4.dp),
            border = BorderStroke(width = 2.dp, color = Color.DarkGray),
            modifier = Modifier.fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            Text(
                text = factura,
                Modifier.padding(16.dp).background(color = Color.Transparent),
                color = Color.DarkGray)
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


