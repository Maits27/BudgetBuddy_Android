package com.example.budgetbuddy.Data

import androidx.compose.ui.graphics.painter.Painter
import androidx.room.OnConflictStrategy

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.budgetbuddy.navigation.AppScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

private val _forceRefresh = MutableStateFlow(false)
val forceRefresh: StateFlow<Boolean>
    get() = _forceRefresh

data class GastoDia(val cantidad: Double, val fecha: LocalDate)
data class GastoTipo(val cantidad: Double, val tipo: TipoGasto)
data class Diseño(val pantalla: AppScreens, val nombre: String, val icono: Painter)

//TODO FECHA CREAR UNA CLASE YO O HACER LA QUE TENGA IKER -> ZonedDateTime

//@Entity(tableName = "gastoDia")
//data class GastoDia(@PrimaryKey val id: String, val nombre: String, val cantidad: Double, val fecha: LocalDate, val tipo: TipoGasto)
//@Entity(tableName = "gastoTipo")
//data class GastoTipo(@PrimaryKey val id: String, val nombre: String, val cantidad: Double, val fecha: LocalDate, val tipo: TipoGasto)
//@Entity(tableName = "diseño")
//data class Diseño(@PrimaryKey val id: Int, val pantalla: AppScreens, val nombre: String, val icono: Painter)
@Entity(tableName = "gasto")
data class Gasto(@PrimaryKey val id: String, var nombre: String, var cantidad: Double, var fecha: String, var tipo: TipoGasto)
//@Entity(tableName = "preferencias")
//data class Preferencias(@PrimaryKey val id: Int, var idioma: Idioma, var fecha: String, var facturaActual: String)

@Dao
interface GastoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertGasto(gasto: Gasto)

    @Delete
    fun deleteGasto(gasto: Gasto)


    @Transaction
    @Query("SELECT * FROM gasto ORDER BY fecha")
    fun todosLosGastos(): List<Gasto>
    @Transaction
    @Query("SELECT * FROM gasto WHERE fecha=:fecha")
    fun elementosFecha(fecha: String): List<Gasto>
    @Transaction
    @Query("SELECT * FROM gasto WHERE tipo=:tipo")
    fun elementosTipo(tipo: TipoGasto): List<Gasto>
    @Transaction
    @Query("SELECT SUM(cantidad) FROM gasto")
    fun gastoTotal(): Double
    @Transaction
    @Query("SELECT SUM(cantidad) FROM gasto WHERE fecha=:fecha")
    fun gastoTotalDia(fecha: String): Double
    @Transaction
    @Query("SELECT SUM(cantidad) FROM gasto WHERE tipo=:tipo")
    fun gastoTotalTipo(tipo: TipoGasto): Double

    @Update
    fun editarGasto(gasto: Gasto)
}


//@Dao
//interface PreferenciasDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertPreferencias(preferencias: Preferencias)
//
//
//    @Query("UPDATE preferencias SET idioma = :idioma")
//    fun cambiarIdioma(idioma: Idioma)
//    @Query("SELECT idioma FROM preferencias")
//    fun conseguirIdioma(): List<Idioma>
//
//
//    @Query("UPDATE preferencias SET facturaActual = :factura")
//    fun cambiarFactura(factura: String)
//    @Query("SELECT facturaActual FROM preferencias")
//    fun conseguirFactura(): List<String>
//
//
//    @Query("UPDATE preferencias SET fecha = :fecha")
//    fun cambiarFecha(fecha: String)
//    @Query("SELECT fecha FROM preferencias")
//    fun conseguirFecha(): List<String>
//}


//@Dao
//interface GastoDiaDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGastoDia(gastoDia: GastoDia)
//    @Query("DELETE FROM gastoDia")
//    fun deleteTablaGastoDia()
//    @Query("SELECT SUM(cantidad) FROM gastoDia")
//    fun gastoTotalDia(): Double
//    @Query("SELECT * FROM gastoDia")
//    fun todosLosGastosDia(): List<GastoDia>
//}
//
//@Dao
//interface GastoTipoDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGastoTipo(gastoTipo: GastoTipo)
//    @Query("DELETE FROM gastoTipo")
//    fun deleteTablaGastoTipo()
//    @Query("SELECT SUM(cantidad) FROM gastoTipo")
//    fun gastoTotalTipo(): Double
//    @Query("SELECT * FROM gastoTipo")
//    fun todosLosGastosTipo(): List<GastoTipo>
//}


