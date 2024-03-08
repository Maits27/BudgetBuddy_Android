package com.example.budgetbuddy.VM

import android.Manifest
import android.os.Environment
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.IGastoRepository
import com.example.budgetbuddy.Data.TipoGasto
import com.example.budgetbuddy.Data.obtenerTipoEnIdioma
import com.example.budgetbuddy.utils.AppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileWriter
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AppViewModel @Inject constructor(
    private val gastoRepository: IGastoRepository,
) : ViewModel() {


    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: Flow<LocalDate> = _fecha

    private val listadoGastos = gastoRepository.todosLosGastos()

    val listadoGastosFecha: (LocalDate)-> Flow<List<Gasto>> = { gastoRepository.elementosFecha(it) }

    val listadoGastosMes: (LocalDate)-> Flow<List<GastoDia>> = { sacarDatosMes(it) }

    val listadoGastosTipo: (LocalDate)-> Flow<List<GastoTipo>> = { sacarDatosPorTipo(it) }


    val totalGasto: (LocalDate)-> Flow<Double> = { gastoRepository.gastoTotalDia(it) }

    var facturaActual: (LocalDate, AppLanguage)->  Flow<String> = { data, idioma->
        listadoGastosFecha(data).map { listaGastos ->
            listaGastos.fold("") { f, gasto -> f + gasto.toString(idioma) }
        }
    }

    fun gastosPrueba(){
        for (cantidad in 1 until 10){
            var i = Random.nextInt(0,6)
            añadirGasto( "Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad+20), TipoGasto.entries[i])
            añadirGasto("Gasto Inicial 1$cantidad", 2.0*cantidad, LocalDate.of(2024,2, cantidad+10), TipoGasto.entries[i])
            i = Random.nextInt(0,6)
            añadirGasto( "Gasto Inicial 2$cantidad", 10.0*cantidad, LocalDate.of(2024,3, cantidad), TipoGasto.entries[i])
            añadirGasto( "Gasto Inicial 5$cantidad", 4.0*cantidad, LocalDate.of(2024,1, cantidad+20), TipoGasto.entries[i])
            añadirGasto( "Gasto Inicial 4$cantidad", 5.0*cantidad, LocalDate.of(2024,1, cantidad+10),TipoGasto.entries[i])
            añadirGasto( "Gasto Inicial 6$cantidad", 1.0*cantidad, LocalDate.now(), TipoGasto.entries[i])
        }
    }

    /*AÑADIR Y ELIMINAR ELEMENTOS*/

    fun añadirGasto(nombre: String, cantidad: Double, fecha: LocalDate, tipo: TipoGasto):Gasto {
        val gasto = Gasto(nombre, cantidad, fecha, tipo)
        try {
            gastoRepository.insertGasto(gasto)
        }catch (e: Exception){
            Log.d("BASE DE DATOS!", e.toString())
        }
        return gasto
    }

    fun borrarGasto(gasto: Gasto){
        gastoRepository.deleteGasto(gasto)
    }

    /*EDITAR ELEMENTOS*/


    fun cambiarFecha(nuevoValor: LocalDate) {
        _fecha.value = nuevoValor
    }


    fun editarGasto(gasto_previo:Gasto, nombre:String, cantidad: Double, fecha:LocalDate, tipo: TipoGasto){
        gastoRepository.editarGasto(Gasto(nombre, cantidad,fecha, tipo, gasto_previo.id))
    }

    /*CALCULAR ELEMENTOS*/



    /*SELECCIONAR ELEMENTOS*/



    /*PRINTEAR ELEMENTOS*/

    fun escribirFecha(fecha: LocalDate): String {
        return "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year}"
    }

    fun escribirMesyAño(fecha: LocalDate): String {
        return "${fecha.monthValue}/${fecha.year}"
    }

    fun fecha_txt(fecha: LocalDate): String {
        return "${fecha.dayOfMonth}_${fecha.monthValue}_${fecha.year}"
    }

    /*PRINTEAR ELEMENTOS GRAFICOS*/

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




