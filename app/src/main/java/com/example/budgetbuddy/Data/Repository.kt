package com.example.budgetbuddy.Data

import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Query
import com.example.budgetbuddy.navigation.AppScreens
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface IGastoRepository{
    fun insertGasto(gasto: Gasto): Unit
    fun deleteGasto(gasto: Gasto): Unit
    fun todosLosGastos():List<Gasto>
    fun elementosFecha(fecha: String): List<Gasto>
    fun elementosTipo(tipo: TipoGasto): List<Gasto>
    fun gastoTotal(): Double
    fun gastoTotalDia(fecha: String): Double
    fun gastoTotalTipo(tipoGasto: TipoGasto): Double
    fun editarGasto(gasto: Gasto): Unit
}
@Singleton
class GastoRepository @Inject constructor(
    private val gastoDao: GastoDao
) : IGastoRepository {
    override fun insertGasto(gasto: Gasto) {
        gastoDao.insertGasto(gasto)
    }

    override fun deleteGasto(gasto: Gasto) {
        gastoDao.deleteGasto(gasto)
    }

    override fun todosLosGastos(): List<Gasto> {
        return gastoDao.todosLosGastos()
    }

    override fun elementosFecha(fecha: String): List<Gasto>{
        return gastoDao.elementosFecha(fecha)
    }

    override fun elementosTipo(tipo: TipoGasto): List<Gasto>{
        return gastoDao.elementosTipo(tipo)
    }

    override fun gastoTotal(): Double{
        return gastoDao.gastoTotal()
    }

    override fun gastoTotalDia(fecha: String): Double{
        return gastoDao.gastoTotalDia(fecha)
    }

    override fun gastoTotalTipo(tipoGasto: TipoGasto): Double{
        return gastoDao.gastoTotalTipo(tipoGasto)
    }

    override fun editarGasto(gasto: Gasto){
        return gastoDao.editarGasto(gasto)
    }
}

//
//
//class PreferenciasRepository @Inject constructor(
//    private val preferenciasDao: PreferenciasDao
//) {
//
//    fun insertPreferencias(preferencias: Preferencias) {
//        preferenciasDao.insertPreferencias(preferencias)
//    }
//
//    fun cambiarIdioma(idioma: Idioma){
//        return preferenciasDao.cambiarIdioma(idioma)
//    }
//    fun conseguirIdioma():List<Idioma>{
//        return preferenciasDao.conseguirIdioma()
//    }
//
//    fun cambiarFactura(factura:String){
//        return preferenciasDao.cambiarFactura(factura)
//    }
//    fun conseguirFactura():List<String>{
//        return preferenciasDao.conseguirFactura()
//    }
//
//    fun cambiarFecha(fecha: String){
//        return preferenciasDao.cambiarFecha(fecha)
//    }
//    fun conseguirFecha():List<String>{
//        return preferenciasDao.conseguirFecha()
//    }
//
//}

//
//class GastoDiaRepository @Inject constructor(
//    private val gastoDiaDao: GastoDiaDao
//) {
//
//    fun insertGastoDia(gasto: GastoDia) {
//        gastoDiaDao.insertGastoDia(gasto)
//    }
//    fun deleteTablaGastoDia(){
//        gastoDiaDao.deleteTablaGastoDia()
//    }
//
//    fun gastoTotalDia(): Double{
//        return gastoDiaDao.gastoTotalDia()
//    }
//
//    fun todosLosGastosDia(): List<GastoDia> {
//        return gastoDiaDao.todosLosGastosDia()
//    }
//
//}
//
//class GastoTipoRepository @Inject constructor(
//    private val gastoTipoDao: GastoTipoDao
//) {
//
//    fun insertGastoTipo(gasto: GastoTipo) {
//        gastoTipoDao.insertGastoTipo(gasto)
//    }
//    fun deleteTablaGastoTipo(){
//        gastoTipoDao.deleteTablaGastoTipo()
//    }
//
//    fun gastoTotalTipo(): Double{
//        return gastoTipoDao.gastoTotalTipo()
//    }
//
//    fun todosLosGastosTipo(): List<GastoTipo> {
//        return gastoTipoDao.todosLosGastosTipo()
//    }
//}
