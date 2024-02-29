package com.example.budgetbuddy.Data

import java.time.LocalDate
import java.util.Date

data class Gasto(val nombre: String, val cantidad: Double, val fecha: LocalDate, val tipo: TipoGasto)
data class GastoDia(val cantidad: Double, val fecha: LocalDate)
data class GastoTipo(val cantidad: Double, val tipo: TipoGasto)

