package com.example.budgetbuddy.Data

import androidx.compose.ui.graphics.painter.Painter

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetbuddy.navigation.AppScreens
import java.time.LocalDate


data class GastoDia(val cantidad: Double, val fecha: LocalDate)
data class GastoTipo(val cantidad: Double, val tipo: TipoGasto)
data class Diseño(val pantalla: AppScreens, val nombre: String, val icono: Painter)


@Entity
data class Gasto(
    @PrimaryKey val id: String,
    var nombre: String,
    var cantidad: Double,
    var fecha: LocalDate,
    var tipo: TipoGasto
)

