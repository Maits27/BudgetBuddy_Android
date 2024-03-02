package com.example.budgetbuddy.Data

import android.app.Application
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDate

@Database(entities = [Gasto::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun gastoDao(): GastoDao
//    abstract fun gastoDiaDao():GastoDiaDao
//    abstract fun gastoTipoDao(): GastoTipoDao
//    abstract fun preferenciasDao(): PreferenciasDao
//    abstract fun diseñoDao(): DiseñoDao
}

/**
 * Type converter for ROOM database
 *
 * These methods convert the given type to a compatible type that SQLite supports and vice versa.
 * ROOM database automatically knows which type converters must use.
 */

