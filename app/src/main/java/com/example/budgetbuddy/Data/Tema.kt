package com.example.budgetbuddy.Data

enum class Tema(val tema: String) {
    Verde("Verde"),
    Azul("Azul"),
    Morado("Morado")
}

fun obtenerTema(tipo: Tema, idioma: String): String {
    // Aquí podrías implementar lógica para seleccionar la cadena de texto correcta según el idioma
    return when (idioma) {
        "eu" -> when (tipo) {
            Tema.Azul -> "Urdina"
            Tema.Verde -> "Orlegia"
            Tema.Morado -> "Morea"
        }
        "en" -> when (tipo) {
            Tema.Azul -> "Blue"
            Tema.Verde -> "Green"
            Tema.Morado -> "Purple"
        }
        else -> tipo.tema// Por defecto, devolver el mensaje original
    }
}