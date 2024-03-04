package com.example.budgetbuddy.preferences

import kotlinx.coroutines.flow.Flow

interface IGeneralPreferences {
    fun language(): Flow<String>
    suspend fun setLanguage(code: String)

    fun getThemePreference(): Flow<String?>

    suspend fun saveThemePreference(theme: String)
//
//    fun themeChoice(): Flow<String>
//    suspend fun setThemeChoice(theme: String)
}