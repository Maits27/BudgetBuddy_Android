package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetbuddy.VM.AppViewModel
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.obtenerTipoEnIdioma
import com.example.budgetbuddy.R
import com.example.budgetbuddy.shared.Header
import com.example.budgetbuddy.shared.NoData
import com.example.budgetbuddy.ui.theme.dashboardTheme
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.time.LocalDate
import java.util.stream.Collectors.groupingBy

@Composable
fun Dashboards(appViewModel: AppViewModel, idioma: String, tema: Int){

    val colors = dashboardTheme(tema)
    /*******************************************************************
     **    Recoger el valor actual de cada flow del AppViewModel      **
     **                 (valor por defecto: initial)                  **
     ******************************************************************/
    val fecha by appViewModel.fecha.collectAsState(initial = LocalDate.now())
    val datosMes by appViewModel.listadoGastosMes(fecha).collectAsState(emptyList())
    val datosTipo by appViewModel.listadoGastosTipo(fecha).collectAsState(emptyList())
    val textoMes = appViewModel.escribirMesyAño(fecha)

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Header(
            titulo = textoMes,
            appViewModel = appViewModel
        )
        Text(text = stringResource(id = R.string.gasto_dia, textoMes), Modifier.padding(top = 16.dp))
        Barras(fecha, datosMes)

        Divider()

        Text(
            text = stringResource(id = R.string.gasto_tipo, textoMes),
            Modifier.padding(top=16.dp)
        )
        LeyendaColores(idioma = idioma, colors = colors, datosTipo = datosTipo)
        Pastel(datosTipo = datosTipo, colors = colors)

        Divider()

    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Barras(
    fecha: LocalDate,
    datosMes: List<GastoDia>
){
    val diasEnMes = obtenerDiasEnMes(fecha)
    var barras = ArrayList<BarChartData.Bar>()
    var kont = 0
    if (datosMes.isEmpty()) {
        NoData()
    } else {
        val diasSinDatos = diasEnMes.filter { dia ->
            datosMes.none { it.fecha.dayOfMonth == dia.dayOfMonth }
        }
        diasEnMes.mapIndexed {index, dia ->
            if (dia in diasSinDatos) {
                if ((diasSinDatos[index-kont].dayOfMonth)%2!=0){
                    barras.add(
                        BarChartData.Bar(
                            label = diasSinDatos[index-kont].dayOfMonth.toString(),
                            value = 0f,
                            color = Color.Gray // Puedes ajustar el color según tus preferencias
                        )
                    )
                }else{
                    barras.add(
                        BarChartData.Bar(
                            label = "",
                            value = 0f,
                            color = Color.Gray // Puedes ajustar el color según tus preferencias
                        )
                    )
                }
            }else{
                if ((datosMes[kont].fecha.dayOfMonth)%2!=0){
                    barras.add(
                        BarChartData.Bar(
                            label = datosMes[kont].fecha.dayOfMonth.toString(),
                            value = datosMes[kont].cantidad.toFloat(),
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }else{
                    barras.add(
                        BarChartData.Bar(
                            label = "",
                            value = datosMes[kont].cantidad.toFloat(),
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
                kont += 1
            }
        }

        BarChart(
            barChartData = BarChartData(barras),
            modifier = Modifier
                .padding(vertical = 30.dp, horizontal = 10.dp)
                .height(200.dp)
                .scale(0.9f),
            labelDrawer = SimpleValueDrawer(
                drawLocation = SimpleValueDrawer.DrawLocation.XAxis
            )
        )
    }
}

// Función para obtener todos los días en el mes
fun obtenerDiasEnMes(fecha: LocalDate): List<LocalDate> {
    val primerDia = fecha.withDayOfMonth(1)
    val ultimoDia = fecha.withDayOfMonth(fecha.month.length(fecha.isLeapYear))

    val listaDias = mutableListOf<LocalDate>()
    var diaActual = primerDia

    while (diaActual.isBefore(ultimoDia) || diaActual.isEqual(ultimoDia)) {
        listaDias.add(diaActual)
        diaActual = diaActual.plusDays(1)
    }

    return listaDias
}

@Composable
fun Pastel(datosTipo: List<GastoTipo>, colors: List<Color>){
    var slices = ArrayList<PieChartData.Slice>()
    when{
        datosTipo.isEmpty() -> {
            NoData()
        }else -> {
            datosTipo.mapIndexed { index, gasto ->
                slices.add(
                    PieChartData.Slice(
                        value = gasto.cantidad.toFloat(),
                        color = colors.get(index)
                    )
                )
            }
            PieChart(
                pieChartData = PieChartData(
                    slices = slices
                ),
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp)
                    .height(250.dp),
                sliceDrawer = SimpleSliceDrawer(
                    sliceThickness = 50f
                )
            )
        }
    }
}

@Composable
fun LeyendaColores(idioma: String,  colors: List<Color>, datosTipo: List<GastoTipo>) {
    when{
        datosTipo.isNotEmpty() -> {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                datosTipo.mapIndexed{index, gasto ->
                    if (index%2 == 0){
                        Row (horizontalArrangement = Arrangement.Center) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .width(150.dp)
                            ) {
                                // Cuadrado de color
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = colors[index],
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .size(20.dp)
                                )
                                // Etiqueta
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    modifier = Modifier.padding(start = 15.dp),
                                    text = obtenerTipoEnIdioma(gasto.tipo, idioma),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            }
                            if (index+1<datosTipo.size){
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    // Cuadrado de color
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = colors[index + 1],
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .size(20.dp)
                                    )
                                    // Etiqueta
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        modifier = Modifier.padding(start = 15.dp),
                                        text = datosTipo[index+1].tipo.tipo,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}