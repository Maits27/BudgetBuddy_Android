package com.example.budgetbuddy.Data

enum class TipoGasto(val tipo: String) {
    Comida("Comida"),
    Hogar("Hogar"),
    Ropa("Ropa"),
    Actividad("Actividad"),
    Transporte("Transporte"),
    Otros("Otros")
}

fun obtenerTipoEnIdioma(tipo: TipoGasto, idioma: String): String {
    // Aquí podrías implementar lógica para seleccionar la cadena de texto correcta según el idioma
    return when (idioma) {
        "eu" -> when (tipo) {
            TipoGasto.Comida -> "Janaria"
            TipoGasto.Hogar -> "Etxea"
            TipoGasto.Ropa -> "Arropa"
            TipoGasto.Actividad -> "Jarduera"
            TipoGasto.Transporte -> "Garraioa"
            TipoGasto.Otros -> "Besteak"
        }
        "en" -> when (tipo) {
            TipoGasto.Comida -> "Food"
            TipoGasto.Hogar -> "Home"
            TipoGasto.Ropa -> "Clothes"
            TipoGasto.Actividad -> "Activity"
            TipoGasto.Transporte -> "Transport"
            TipoGasto.Otros -> "Others"
        }
        else -> tipo.tipo// Por defecto, devolver el mensaje original
    }
}