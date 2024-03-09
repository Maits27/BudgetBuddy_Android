package com.example.budgetbuddy.Data

import com.example.budgetbuddy.Data.Enumeration.TipoGasto
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton


/******************************************************************
 * Interfaz que define la API del listado de gastos - Repositorio
 * Los métodos definidos son las acciones posibles para interactuar
 * con la BBDD
 *******************************************************************/
interface IGastoRepository{
    fun insertGasto(gasto: Gasto)
    suspend fun insertGastos(gastos: List<Gasto>): List<Unit>
    fun deleteGasto(gasto: Gasto): Int
    fun todosLosGastos(): Flow<List<Gasto>>
    fun elementosFecha(fecha: LocalDate): Flow<List<Gasto>>
    fun gastoTotal(): Flow<Double>
    fun gastoTotalDia(fecha: LocalDate): Flow<Double>
    fun gastosIsEmpty(): Boolean
    fun tipoIsEmpty(tipo: TipoGasto): Boolean
    fun diaIsEmpty(fecha: LocalDate): Boolean
    fun editarGasto(gasto: Gasto): Int
}
/**
 * Implementación de [IGastoRepository] que usa Hilt para inyectar los
 * parámetros necesarios. Desde aquí se accede a [GastoDao], que se encarga
 * de la conexión a la BBDD de Room.
 * */
@Singleton
class GastoRepository @Inject constructor(
    private val gastoDao: GastoDao
) : IGastoRepository {
    override  fun insertGasto(gasto: Gasto) {
        return gastoDao.insertGasto(gasto)
    }

    override suspend fun insertGastos(gastos: List<Gasto>): List<Unit>{
        return gastoDao.insertGastos(gastos)
    }

    override fun deleteGasto(gasto: Gasto): Int {
        return gastoDao.deleteGasto(gasto)
    }

    override fun todosLosGastos(): Flow<List<Gasto>> {
        return gastoDao.todosLosGastos()
    }

    override fun elementosFecha(fecha: LocalDate): Flow<List<Gasto>>{
        return gastoDao.elementosFecha(fecha)
    }

    override fun gastoTotal(): Flow<Double>{
        return gastoDao.gastoTotal()
    }

    override fun gastoTotalDia(fecha: LocalDate): Flow<Double>{
        return gastoDao.gastoTotalDia(fecha)
    }

    override fun gastosIsEmpty(): Boolean {
        if (gastoDao.cuantosGastos()==0){
            return true
        }
        return false
    }

    override fun diaIsEmpty(fecha: LocalDate): Boolean {
        if (gastoDao.cuantosDeDia(fecha)==0){
            return true
        }
        return false
    }

    override fun tipoIsEmpty(tipo: TipoGasto): Boolean {
        if (gastoDao.cuantosDeTipo(tipo)==0){
            return true
        }
        return false
    }

    override fun editarGasto(gasto: Gasto): Int{
        return gastoDao.editarGasto(gasto)
    }
}

