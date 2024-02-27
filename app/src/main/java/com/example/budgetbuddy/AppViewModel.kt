package com.example.budgetbuddy

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.Idioma

class AppViewModel: ViewModel() {

    var idioma by mutableStateOf(Idioma.ES)
        private set
    var listadoGastos: MutableList<Gasto> = mutableListOf()
        private set

    init {
        // Código a ejecutar al iniciar el ViewModel
        for (cantidad in 1..10){
            añadirGasto("Gasto Inicial $cantidad", 1.0*cantidad)
        }
        Log.d("appviewmodel", listadoGastos.toString())
    }
    fun añadirGasto(nombre: String, cantidad: Double){
        if (nombre != ""){
            if (cantidad>0.0){
                listadoGastos.add(Gasto(nombre, cantidad))
            }
        }
    }
    fun gastoTotal(): Double{
        var gasto: Double = 0.0
        if (!listadoGastos.isEmpty()){
            for (g in listadoGastos){
                gasto = gasto + g.cantidad
            }
        }
        return gasto
    }

    fun cambiarIdioma(code:String){
        for (i in Idioma.entries){
            if (code == i.code) {
                idioma = i
            }
        }
    }

}

