package com.example.budgetbuddy

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.security.MessageDigest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AppViewModel @Inject constructor(
    private val gastoRepository: IGastoRepository, //IGastoRepository -> App module
//    private val gastoDiaRepository: GastoDiaRepository,
//    private val gastoTipoRepository: GastoTipoRepository,
//    private val preferenciasRepository: PreferenciasRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var idioma by mutableStateOf(Idioma.ES)
        private set
    var fecha by mutableStateOf(LocalDate.now())
        private set

    var facturaActual by mutableStateOf("")
        private set



    private val _forceRefresh = MutableStateFlow(false)
    val forceRefresh: StateFlow<Boolean>
        get() = _forceRefresh

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, formatter) }
    }

    init {
        // Código a ejecutar al iniciar el ViewModel
        gastosPrueba()
    }

    private fun gastosPrueba(){
        for (cantidad in 1 until 10){
            añadirGasto( "Gasto Inicial $cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad), TipoGasto.Comida)
            añadirGasto("Gasto Inicial 1$cantidad", 1.0*cantidad, LocalDate.of(2024,2, cantidad+10), TipoGasto.Transporte)
            añadirGasto( "Gasto Inicial 2$cantidad", 1.0*cantidad, LocalDate.of(2024,1, cantidad+10), TipoGasto.Actividad)
            añadirGasto( "Gasto Inicial 3$cantidad", 1.0*cantidad, LocalDate.now(), TipoGasto.Otros)
        }
    }

    /*AÑADIR Y ELIMINAR ELEMENTOS*/

    fun añadirGasto(nombre: String, cantidad: Double, fecha: LocalDate, tipo: TipoGasto):Gasto {
        val id = hashString("${nombre}${Random.nextInt(1, 101)}")
        val gasto = Gasto(id, nombre, cantidad, fromLocalDate(fecha)?:"", tipo)
        try {
            gastoRepository.insertGasto(gasto)
        }catch (e: Exception){
            Log.d("BASE DE DATOS", e.toString())
        }
        return gasto
    }

    fun borrarGasto(gasto: Gasto){
        gastoRepository.deleteGasto(gasto)
//        cambiarGastosFecha()
        this.triggerRefresh()
    }

    private fun crearElementosFactura(): String{
        var factura = ""
        val fecha = fromLocalDate(fecha())?:""
        for (gasto in gastoRepository.elementosFecha(fecha)){
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
//        cambiarGastosFecha()
    }

//    private fun cambiarGastosFecha(){
//        val fechaAhora = preferenciasRepository.conseguirFecha()[0]
//
//        for (gasto in gastoRepository.todosLosGastos()){
//            if (gasto.fecha == fechaAhora){
//                var gastoDia = GastoDia(gasto.id, gasto.nombre, gasto.cantidad, gasto.fecha, gasto.tipo)
//                gastoDiaRepository.insertGastoDia(gastoDia)
//            }
//        }
//        this.triggerRefresh()
//    }


    fun editarGasto(gasto_previo:Gasto, nombre:String, cantidad: Double, fecha:LocalDate, tipo: TipoGasto){
        gastoRepository.editarGasto(Gasto(gasto_previo.id, nombre, cantidad,fromLocalDate(fecha)?:"", tipo))
        this.triggerRefresh()
//        cambiarGastosFecha()
    }

    /*CALCULAR ELEMENTOS*/

    fun gastoTotal(): Double{
        return gastoRepository.gastoTotal()
    }

    fun gastoTotalFecha(fecha: LocalDate=fecha()): Double{
        return gastoRepository.gastoTotalDia(fromLocalDate(fecha)?:"")
    }

    fun gastoTotalTipo(tipo: TipoGasto): Double{
        return gastoRepository.gastoTotalTipo(tipo)
    }

    /*SELECCIONAR ELEMENTOS*/

    fun todosLosGastos(): List<Gasto> {
        return gastoRepository.todosLosGastos()
    }

    fun todosLosGastosFecha(fecha: LocalDate=fecha()): List<Gasto> {
        return gastoRepository.elementosFecha(fromLocalDate(fecha)?:"")
    }

    fun todosLosGastosTipo(tipoGasto: TipoGasto): List<Gasto> {
        return gastoRepository.elementosTipo(tipoGasto)
    }

    fun idioma(): Idioma{
        return idioma
    }
    fun factura(): String{
        return facturaActual
    }
    fun fecha(): LocalDate{
        return fecha
    }

    /*PRINTEAR ELEMENTOS*/

    fun escribirFecha(fecha: LocalDate = fecha()): String {
        return "${fecha.dayOfMonth}/${fecha.monthValue}/${fecha.year}"
    }

    fun escribirMesyAño(fecha: LocalDate = fecha()): String {
        return "${fecha.monthValue}/${fecha.year}"
    }

    fun fecha_txt(fecha: LocalDate = fecha()): String {
        return "${fecha.dayOfMonth}_${fecha.monthValue}_${fecha.year}"
    }

    /*PRINTEAR ELEMENTOS GRAFICOS*/

    fun sacarDatosMes(): MutableList<GastoDia>{
        val fecha = fecha()
        var datos = mutableMapOf<LocalDate, Double>()
        var datosMes = mutableListOf<GastoDia>()
        for (gasto in gastoRepository.todosLosGastos()){
            val gastoFecha = toLocalDate(gasto.fecha)?: LocalDate.now()
            if (gastoFecha.monthValue == fecha.monthValue){
                if (gastoFecha.year == fecha.year){
                    var suma = datos.getOrDefault(gastoFecha, 0.0)
                    datos[gastoFecha] = suma + gasto.cantidad
                }
            }
        }
        for ((date, value) in datos) {
            datosMes.add(GastoDia(value, date))
        }
        return datosMes
    }

    fun sacarDatosPorTipo(): MutableList<GastoTipo>{
        val fecha = fecha()
        var datos = mutableMapOf<TipoGasto, Double>()
        var datosTipo = mutableListOf<GastoTipo>()

        for (gasto in gastoRepository.todosLosGastos()){
            val gastoFecha = toLocalDate(gasto.fecha)?: LocalDate.now()
            if (gastoFecha.monthValue == fecha.monthValue){
                if (gastoFecha.year == fecha.year) {
                    var suma = datos.getOrDefault(gasto.tipo, 0.0)
                    datos[gasto.tipo] = suma + gasto.cantidad
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




    private fun triggerRefresh() {
        _forceRefresh.value = true
    }

    // Esta función se llama cuando se ha completado la actualización
    fun refreshComplete() {
        _forceRefresh.value = false
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

