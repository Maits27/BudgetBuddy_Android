package com.example.budgetbuddy

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.TypeConverter
import com.example.budgetbuddy.Data.Diseño
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoRepository
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.IGastoRepository
import com.example.budgetbuddy.Data.Idioma
import com.example.budgetbuddy.Data.TipoGasto
import com.example.budgetbuddy.navigation.AppScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AppViewModel @Inject constructor(
    private val gastoRepository: IGastoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var idioma by mutableStateOf(Idioma.ES)
        private set
    var fecha by mutableStateOf(LocalDate.now())

    val listadoGastosFecha = gastoRepository.elementosFecha(fecha)

    val listadoGastos = gastoRepository.todosLosGastos()

    val totalGasto = gastoRepository.gastoTotalDia(fecha)
    var facturaActual: Flow<String> =  listadoGastosFecha.map {
        listaGastos -> listaGastos.fold(""){ f, gasto -> f + gasto.toString() }
    }

    init {
        gastosPrueba()
    }


    fun gastosPrueba(){
        for (cantidad in 1 until 10){
            añadirGasto( "Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad), TipoGasto.Comida)
            añadirGasto("Gasto Inicial 1$cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad+10), TipoGasto.Transporte)
            añadirGasto( "Gasto Inicial 2$cantidad", 1.0*cantidad, LocalDate.of(2024,1, cantidad+10), TipoGasto.Actividad)
            añadirGasto( "Gasto Inicial 3$cantidad", 1.0*cantidad, LocalDate.now(), TipoGasto.Otros)
        }
    }

    /*AÑADIR Y ELIMINAR ELEMENTOS*/

    fun añadirGasto(nombre: String, cantidad: Double, fecha: LocalDate, tipo: TipoGasto):Gasto {
        val gasto = Gasto(nombre, cantidad, fecha, tipo)
        try {
            gastoRepository.insertGasto(gasto)
        }catch (e: Exception){
            Log.d("BASE DE DATOS", e.toString())
        }
        return gasto
    }

    fun borrarGasto(gasto: Gasto){
        gastoRepository.deleteGasto(gasto)
    }



    /*EDITAR ELEMENTOS*/



    fun editarGasto(gasto_previo:Gasto, nombre:String, cantidad: Double, fecha:LocalDate, tipo: TipoGasto){
        gastoRepository.editarGasto(Gasto(nombre, cantidad,fecha, tipo, gasto_previo.id))
    }

    /*CALCULAR ELEMENTOS*/



    fun gastoTotalTipo(tipo: TipoGasto): Flow<Double>{
        return gastoRepository.gastoTotalTipo(tipo)
    }

    /*SELECCIONAR ELEMENTOS*/


    fun todosLosGastosTipo(tipoGasto: TipoGasto): List<Gasto> {
        return gastoRepository.elementosTipo(tipoGasto)
    }

    /*PRINTEAR ELEMENTOS*/

    fun escribirFecha(fecha: LocalDate = this.fecha): String {
        return "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year}"
    }

    fun escribirMesyAño(fecha: LocalDate = this.fecha): String {
        return "${fecha.monthValue}/${fecha.year}"
    }

    fun fecha_txt(fecha: LocalDate = this.fecha): String {
        return "${fecha.dayOfMonth}_${fecha.monthValue}_${fecha.year}"
    }

    /*PRINTEAR ELEMENTOS GRAFICOS*/

    fun sacarDatosMes(): MutableList<GastoDia>{
        var datos = mutableMapOf<LocalDate, Double>()
        var datosMes = mutableListOf<GastoDia>()
        listadoGastos.map { gastos->
            for (gasto in gastos){
                val gastoFecha = gasto.fecha
                if (gastoFecha.monthValue == fecha.monthValue){
                    if (gastoFecha.year == fecha.year){
                        var suma = datos.getOrDefault(gastoFecha, 0.0)
                        datos[gastoFecha] = suma + gasto.cantidad
                    }
                }
            }
        }
        for ((date, value) in datos) {
            datosMes.add(GastoDia(value, date))
        }
        return datosMes
    }

    fun sacarDatosPorTipo(): MutableList<GastoTipo>{
        var datos = mutableMapOf<TipoGasto, Double>()
        var datosTipo = mutableListOf<GastoTipo>()

        listadoGastos.map { gastos ->
            for (gasto in gastos){
                val gastoFecha = gasto.fecha
                if (gastoFecha.monthValue == fecha.monthValue){
                    if (gastoFecha.year == fecha.year) {
                        var suma = datos.getOrDefault(gasto.tipo, 0.0)
                        datos[gasto.tipo] = suma + gasto.cantidad
                    }
                }
            }

        }
        for ((type, value) in datos) {
            datosTipo.add(GastoTipo(value, type))
        }
        return datosTipo
    }


    /*PREFERENCIAS*/

    fun cambiarIdioma(code:String){
        for (i in Idioma.entries){
            if (code == i.code) {
                idioma = i
            }
        }
    }
}


/*SEGURIDAD*/
//
//fun hashString(input: String, algorithm: String = "SHA-256"): String {
//    val bytes = input.toByteArray()
//    val digest = MessageDigest.getInstance(algorithm)
//    val hashedBytes = digest.digest(bytes)
//
//    return bytesToHex(hashedBytes)
//}
//
//fun bytesToHex(bytes: ByteArray): String {
//    val hexChars = CharArray(bytes.size * 2)
//    for (i in bytes.indices) {
//        val v = bytes[i].toInt() and 0xFF
//        hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
//        hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
//    }
//    return String(hexChars)
//}

