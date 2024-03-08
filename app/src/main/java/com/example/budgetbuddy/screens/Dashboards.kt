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
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.time.LocalDate
import java.util.stream.Collectors.groupingBy

@Composable
fun Dashboards(appViewModel: AppViewModel, idioma: String){

    var showCalendar by remember { mutableStateOf(false) }
    var colors = mutableListOf(
        Color(0xffB48EF0),
        Color(0xff8EBEF0),
        Color(0xff2760CE),
        Color(0xff3EBE52),
        Color(0xffF0CA8E),
        Color(0xffF08E8E),
    )
    /*******************************************************************
     **    Recoger el valor actual de cada flow del AppViewModel      **
     **                 (valor por defecto: initial)                  **
     ******************************************************************/
    val fecha by appViewModel.fecha.collectAsState(initial = LocalDate.now())
    val datosMes by appViewModel.listadoGastosMes(fecha).collectAsState(emptyList())
    val datosTipo by appViewModel.listadoGastosTipo(fecha).collectAsState(emptyList())
    val data by appViewModel.mapaDatosPorTipo(fecha, idioma).collectAsState(emptyMap())
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

        Text(
            text = stringResource(id = R.string.gasto_tipo, textoMes),
            Modifier.padding(top=16.dp)
        )
        LeyendaColores(idioma = idioma, colors = colors, datosTipo = datosTipo)
        Pastel(datosTipo = datosTipo, colors = colors)

//        PieChart(data)

        Divider(Modifier.padding(top=50.dp))
        Text(text = stringResource(id = R.string.gasto_dia, textoMes), Modifier.padding(top = 16.dp))
        Barras(fecha, datosMes)

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
                .height(300.dp)
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
fun PieChart(
    data: Map<String, Int>,
    radiusOuter: Dp = 150.dp,
    chartBarWidth: Dp = 65.dp,
    animDuration: Int = 1000,
) {

    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    // To set the value of each Arc according to
    // the value given in the data, we have used a simple formula.
    // For a detailed explanation check out the Medium Article.
    // The link is in the about section and readme file of this GitHub Repository
    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    // add the colors as per the number of data(no. of pie chart entries)
    // so that each data will get a color
    val colors = listOf(
        Color(0xffB48EF0),
        Color(0xff8EBEF0),
        Color(0xff2760CE),
        Color(0xff3EBE52),
        Color(0xffF0CA8E),
        Color(0xffF08E8E),
    )

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value*1.2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // To see the data in more structured way
        // Compose Function in which Items are showing data
//        DetailsPieChart(
//            data = data,
//            colors = colors
//        )

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 1.2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

    }

}

@Composable
fun DetailsPieChart(
    data: Map<String, Int>,
    colors: List<Color>
) {
    Column(
        modifier = Modifier
            .padding( bottom = 40.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        data.values.forEachIndexed { index, value ->
            Row (
                horizontalArrangement = Arrangement.Center
            ) {
                if (index % 2 == 0) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(4.dp)
                            .width(150.dp)
                    ) {
                        DetailsPieChartItem(
                            data = Pair(data.keys.elementAt(index), value),
                            color = colors[index]
                        )
                    }
                    if (index + 1 < data.size) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            DetailsPieChartItem(
                                data = Pair(
                                    data.keys.elementAt(index + 1),
                                    data.values.elementAt(index + 1)
                                ),
                                color = colors[index]
                            )
                        }
                    }
                } else if (index + 1 == data.size) {
                    DetailsPieChartItem(
                        data = Pair(
                            data.keys.elementAt(index + 1),
                            data.values.elementAt(index + 1)
                        ),
                        color = colors[index]
                    )
                }
            }
        }
    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Int>,
    height: Dp = 20.dp,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(4.dp)
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(10.dp)
                )
                .size(height)
        )

        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = data.first,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color.Black
        )

    }
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