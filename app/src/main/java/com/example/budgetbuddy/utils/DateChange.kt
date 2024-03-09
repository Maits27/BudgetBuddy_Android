package com.example.budgetbuddy.utils

import java.time.LocalDate


/**
 * Funciones de tipo [LocalDate] y [Long] para conversión entre ellos
 * fuera del entorno ROOM.
 */
fun LocalDate.toLong(): Long{
    return this.toEpochDay()
}

fun Long.toLocalDate(): LocalDate {
    return this.let { LocalDate.ofEpochDay(this) }
}
