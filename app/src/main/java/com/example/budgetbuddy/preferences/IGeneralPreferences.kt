package com.example.budgetbuddy.preferences

import kotlinx.coroutines.flow.Flow

interface IGeneralPreferences {
    fun language(): Flow<String>
    suspend fun setLanguage(code: String)

    fun getThemePreference(): Flow<Int>

    suspend fun saveThemePreference(theme: Int)

    suspend fun primero()
    fun getPrimero(): Flow<Boolean>
//
//    fun themeChoice(): Flow<String>
//    suspend fun setThemeChoice(theme: String)
}