package com.example.budgetbuddy.VM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbuddy.Data.Enumeration.AppLanguage
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.IGastoRepository
import com.example.budgetbuddy.Data.Enumeration.TipoGasto
import com.example.budgetbuddy.preferences.IGeneralPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

/********************************************************
 ****                 App View Model                 ****
 ********************************************************/
/**
 * View Model de Hilt para los datos del usuario
 * Encargado de las interacciones entre el frontend de la app y el repositorio [gastoRepository] que realiza los cambios en ROOM.
 *
 * @gastoRepository: implementación de [IGastoRepository] y repositorio a cargo de realizar los cambios en la BBDD.
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val gastoRepository: IGastoRepository,
) : ViewModel() {

    /*************************************************
     **                    Estados                  **
     *************************************************/

    // Flows a los que les llega constantemente las actualizaciones y datos de la BBDD.
    // De esta forma no es necesaria una actualización cada vez que se realice un cambio.

    private val _fecha = MutableStateFlow(LocalDate.now())

    val fecha: Flow<LocalDate> = _fecha

    private val listadoGastos = gastoRepository.todosLosGastos()

    val listadoGastosFecha: (LocalDate)-> Flow<List<Gasto>> = { gastoRepository.elementosFecha(it) }

    val listadoGastosMes: (LocalDate)-> Flow<List<GastoDia>> = { sacarDatosMes(it) }

    val listadoGastosTipo: (LocalDate)-> Flow<List<GastoTipo>> = { sacarDatosPorTipo(it) }

    val totalGasto: (LocalDate)-> Flow<Double> = { gastoRepository.gastoTotalDia(it) }

    var facturaActual: (LocalDate, AppLanguage)->  Flow<String> = { data, idioma->
        listadoGastosFecha(data).map { listaGastos ->
            listaGastos.fold("") { f, gasto -> f + "\t- " + gasto.toString(idioma) }
        }
    }

    /*************************************************
     **          Inicialización de la BBDD          **
     *************************************************/
//    init {
//        viewModelScope.launch {
//            for (cantidad in 1 until 10){
//                Log.d("BD-Preloading", "Loading cantidad $cantidad")
//                añadirGasto( "Gasto Inicial 2$cantidad", 10.0*cantidad, LocalDate.of(2024,3, cantidad), TipoGasto.Comida)
//                añadirGasto( "Gasto Inicial 5$cantidad", 4.0*cantidad, LocalDate.of(2024,3, cantidad+20), TipoGasto.Ropa)
//                añadirGasto( "Gasto Inicial 4$cantidad", 5.0*cantidad, LocalDate.of(2024,3, cantidad+10), TipoGasto.Hogar)
//                añadirGasto( "Gasto Inicial 6$cantidad", 10.0*cantidad, LocalDate.of(2024,4, cantidad), TipoGasto.Transporte)
//                añadirGasto( "Gasto Inicial 7$cantidad", 4.0*cantidad, LocalDate.of(2024,4, cantidad+20), TipoGasto.Comida)
//                añadirGasto( "Gasto Inicial 8$cantidad", 5.0*cantidad, LocalDate.of(2024,4, cantidad+10), TipoGasto.Actividad)
//                añadirGasto( "Gasto Inicial 9$cantidad", 1.0*cantidad, LocalDate.now(), TipoGasto.Otros)
//            }
//        }
//    }


    /*************************************************
     **                    Eventos                  **
     *************************************************/


    ////////////////////// Añadir y eliminar elementos //////////////////////

    suspend fun añadirGasto(nombre: String, cantidad: Double, fecha: LocalDate, tipo: TipoGasto):Gasto {
        val gasto = Gasto(nombre, cantidad, fecha, tipo)
        try {
            gastoRepository.insertGasto(gasto)
        }catch (e: Exception){
            Log.d("BASE DE DATOS!", e.toString())
        }
        return gasto
    }

    suspend fun borrarGasto(gasto: Gasto){
        gastoRepository.deleteGasto(gasto)
    }

    ////////////////////// Editar elementos //////////////////////


    fun cambiarFecha(nuevoValor: LocalDate) {
        _fecha.value = nuevoValor

    }
    fun editarGasto(gasto_previo:Gasto, nombre:String, cantidad: Double, fecha:LocalDate, tipo: TipoGasto){
        gastoRepository.editarGasto(Gasto(nombre, cantidad,fecha, tipo, gasto_previo.id))
    }

    ////////////////////// Pasar a formato String //////////////////////

    fun escribirFecha(fecha: LocalDate): String {
        return "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year}"
    }

    fun escribirMesyAño(fecha: LocalDate): String {
        return "${fecha.monthValue}/${fecha.year}"
    }

    fun fecha_txt(fecha: LocalDate): String {
        return "${fecha.dayOfMonth}_${fecha.monthValue}_${fecha.year}"
    }

    ////////////////////// Recopilar datos gráficos //////////////////////

    fun sacarDatosMes(fecha: LocalDate): Flow<List<GastoDia>>{
        val gastosFechados = listadoGastos.map{
            it.filter { gasto -> gasto.fecha.year == fecha.year }
                .filter { gasto ->  gasto.fecha.monthValue == fecha.monthValue }
        }
        val gastosAgrupados = gastosFechados.map {
            it.groupBy { gasto -> gasto.fecha }.map { (fecha, gastos) ->
                GastoDia(
                    cantidad = gastos.sumByDouble { it.cantidad },
                    fecha = fecha
                )
            }
        }
        return gastosAgrupados
    }

    fun sacarDatosPorTipo(fecha: LocalDate): Flow<List<GastoTipo>>{
        val gastosFechados = listadoGastos.map{
            it.filter { gasto -> gasto.fecha.year == fecha.year }
            .filter { gasto ->  gasto.fecha.monthValue == fecha.monthValue }
        }
        val gastosAgrupados = gastosFechados.map {
            it.groupBy { gasto -> gasto.tipo }.map { (tipo, gastos) ->
                GastoTipo(
                    cantidad = gastos.sumByDouble { it.cantidad },
                    tipo = tipo
                )
            }
        }
        return gastosAgrupados
    }
}




