package com.example.budgetbuddy.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.TipoGasto

@Dao
interface GastoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGasto(gasto: Gasto)

    suspend fun insertGastos(gastos: List<Gasto>) = gastos.map { insertGasto(it) }

    @Delete
    fun deleteGasto(gasto: Gasto): Int

    @Transaction
    @Query("SELECT * FROM Gasto ORDER BY fecha")
    fun todosLosGastos(): Flow<List<Gasto>>
    @Transaction
    @Query("SELECT * FROM Gasto WHERE fecha=:fecha")
    fun elementosFecha(fecha: LocalDate): Flow<List<Gasto>>
    @Transaction
    @Query("SELECT * FROM Gasto WHERE tipo=:tipo")
    fun elementosTipo(tipo: TipoGasto): List<Gasto>
    @Transaction
    @Query("SELECT SUM(cantidad) FROM Gasto")
    fun gastoTotal(): Flow<Double>
    @Transaction
    @Query("SELECT SUM(cantidad) FROM Gasto WHERE fecha=:fecha")
    fun gastoTotalDia(fecha: LocalDate): Flow<Double>
    @Transaction
    @Query("SELECT SUM(cantidad) FROM Gasto WHERE tipo=:tipo")
    fun gastoTotalTipo(tipo: TipoGasto): Flow<Double>
    @Transaction
    @Query("SELECT COUNT(*) FROM Gasto")
    fun cuantosGastos(): Int
    @Transaction
    @Query("SELECT COUNT(*) FROM Gasto WHERE fecha=:fecha")
    fun cuantosDeDia(fecha: LocalDate): Int
    @Transaction
    @Query("SELECT COUNT(*) FROM Gasto WHERE tipo=:tipo")
    fun cuantosDeTipo(tipo: TipoGasto): Int

    @Update
    fun editarGasto(gasto: Gasto): Int
}
