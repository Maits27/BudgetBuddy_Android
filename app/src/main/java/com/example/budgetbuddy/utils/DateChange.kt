package com.example.budgetbuddy.utils

import java.time.LocalDate

fun LocalDate.toLong(): Long{
    return this.toEpochDay()
}

fun Long.toLocalDate(): LocalDate {
    return this.let { LocalDate.ofEpochDay(this) }
}
