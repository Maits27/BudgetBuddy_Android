package com.example.budgetbuddy.Data

enum class ExpenseFilter(val title: String) {
    All("Todos"),
    ThisMonth("Este Mes"),
    LastMonth("Mes Pasado"),
    ThisYear("Este Año"),
    Custom("Personalizado")
}