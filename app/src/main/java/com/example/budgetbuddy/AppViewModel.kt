package com.example.budgetbuddy

import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.GastoDia
import com.example.budgetbuddy.Data.GastoTipo
import com.example.budgetbuddy.Data.Idioma
import com.example.budgetbuddy.Data.TipoGasto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel: ViewModel() {

    private val _forceRefresh = MutableStateFlow(false)
    val forceRefresh: StateFlow<Boolean>
        get() = _forceRefresh


    var idioma by mutableStateOf(Idioma.ES)
        private set
    var fecha by mutableStateOf(LocalDate.now())
        private set
    var listadoGastosFecha: MutableList<Gasto> = mutableListOf()
        private set
    var listadoGastos: MutableList<Gasto> = mutableListOf()
        private set

    var facturaActual by mutableStateOf("")
        private set


    /*INICIALIZAR DATOS*/
    init {
        // Código a ejecutar al iniciar el ViewModel
        for (cantidad in 1..10){
            añadirGasto("Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad), TipoGasto.Comida)
            añadirGasto("Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad+10), TipoGasto.Transporte)
            añadirGasto("Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.of(2024,1, cantidad+10), TipoGasto.Actividad)
            añadirGasto("Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.now(), TipoGasto.Otros)
        }
        cambiarGastosFecha()
    }

    /*AÑADIR Y ELIMINAR ELEMENTOS*/

    fun añadirGasto(nombre: String, cantidad: Double, fecha: LocalDate, tipo:TipoGasto){
        if (nombre != ""){
            if (cantidad>0.0){
                listadoGastos.add(Gasto(nombre, cantidad, fecha, tipo))
            }
        }
        cambiarFecha(fecha)
        this.triggerRefresh()
    }

    fun borrarGasto(gasto: Gasto){
        listadoGastos.remove(gasto)
        this.triggerRefresh()
    }

    private fun crearElementosFactura(): String{
        var factura = ""
        for (gasto in listadoGastosFecha){
            factura += "${gasto.nombre} (${gasto.tipo.tipo}):\t\t${gasto.cantidad}€\n"
        }
        return factura+"\n"
    }

    /*EDITAR ELEMENTOS*/

    fun cambiarFactura(init: String, end:String):String{
        facturaActual = init+crearElementosFactura()+end
        this.triggerRefresh()
        return facturaActual
    }
    fun cambiarFecha(nueva_fecha: LocalDate){
        fecha = nueva_fecha
        cambiarGastosFecha()
    }
    private fun cambiarGastosFecha(){
        listadoGastosFecha = mutableListOf()
        for (gasto in listadoGastos){
            if (gasto.fecha == fecha){
                listadoGastosFecha.add(gasto)
            }
        }
        this.triggerRefresh()
    }

    fun editarGasto(gasto_previo:Gasto, nombre:String, cantidad: Double, fecha:LocalDate, tipo: TipoGasto){
        listadoGastos.remove(gasto_previo)
        añadirGasto(nombre, cantidad, fecha, tipo)
        this.triggerRefresh()
        cambiarGastosFecha()
    }

    /*CALCULAR ELEMENTOS*/

    fun gastoTotal(): Double{
        var gasto: Double = 0.0
        if (!listadoGastos.isEmpty()){
            for (g in listadoGastos){
                gasto = gasto + g.cantidad
            }
        }
        return gasto
    }

    fun gastoTotalFecha(): Double{
        var gasto: Double = 0.0
        if (!listadoGastosFecha.isEmpty()){
            for (g in listadoGastosFecha){
                gasto = gasto + g.cantidad
            }
        }
        return gasto
    }

    /*PRINTEAR ELEMENTOS*/

    fun escribirFecha(fecha: LocalDate = this.fecha): String {
        return "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year}"
    }

    fun escribirMesyAño(fecha: LocalDate = this.fecha): String {
        return "${fecha.monthValue}/${fecha.year}"
    }


    /*PRINTEAR ELEMENTOS GRAFICOS*/

    fun sacarDatosMes(): MutableList<GastoDia>{
        var datos = mutableMapOf<LocalDate, Double>()
        var datosMes = mutableListOf<GastoDia>()
        for (gasto in listadoGastos){
            if (gasto.fecha.monthValue == fecha.monthValue){
                if (gasto.fecha.year == fecha.year){
                    var suma = datos.getOrDefault(gasto.fecha, 0.0)
                    datos[gasto.fecha] = suma + gasto.cantidad
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
        for (gasto in listadoGastos){
            var suma = datos.getOrDefault(gasto.tipo, 0.0)
            datos[gasto.tipo] = suma + gasto.cantidad
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

    /*SEGURIDAD*/

    fun hashString(input: String, algorithm: String = "SHA-256"): String {
        val bytes = input.toByteArray()
        val digest = MessageDigest.getInstance(algorithm)
        val hashedBytes = digest.digest(bytes)

        return bytesToHex(hashedBytes)
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }

    private fun triggerRefresh() {
        _forceRefresh.value = true
    }

    // Esta función se llama cuando se ha completado la actualización
    fun refreshComplete() {
        _forceRefresh.value = false
    }

}

