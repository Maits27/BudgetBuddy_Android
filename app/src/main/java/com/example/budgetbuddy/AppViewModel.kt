package com.example.budgetbuddy

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.IGastoRepository
import com.example.budgetbuddy.Data.TipoGasto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val gastoRepository: IGastoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: Flow<LocalDate> = _fecha

    val listadoGastosFecha: (LocalDate)-> Flow<List<Gasto>> = { gastoRepository.elementosFecha(it) }

    val listadoGastos = gastoRepository.todosLosGastos()

    val totalGasto: (LocalDate)-> Flow<Double> = { gastoRepository.gastoTotalDia(it) }

    var facturaActual: (LocalDate)->  Flow<String> = {
        listadoGastosFecha(it).map { listaGastos ->
            listaGastos.fold("") { f, gasto -> f + gasto.toString() }
        }
    }

    val listadoGastosMes: (LocalDate)-> Flow<List<GastoDia>> = { sacarDatosMes(it) }

    val listadoGastosTipo: (LocalDate)-> Flow<List<GastoTipo>> = { sacarDatosPorTipo(it) }

//    init {
//        Log.d("VIEW MODEL", "INICIALIZANDO DATOS")
//        gastosPrueba()
//        Log.d("VIEW MODEL", "DATOS INICIALIZADOS")
//    }

    fun cambiarFecha(nuevoValor: LocalDate) {
        _fecha.value = nuevoValor
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
            Log.d("BASE DE DATOS!!!!!!!!!!!!!!!!!!!!!!!", e.toString())
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
        val gastosFechados = listadoGastos.map{ it.filter { gasto -> gasto.fecha.year == fecha.year } }
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
        val gastosFechados = listadoGastos.map{ it.filter { gasto -> gasto.fecha.year == fecha.year } }
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




