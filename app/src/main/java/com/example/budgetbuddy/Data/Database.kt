package com.example.budgetbuddy.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetbuddy.utils.Converters

@Database(entities = [Gasto::class], version = 2)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun gastoDao(): GastoDao
}


/**
 * Type converter for ROOM database
 *
 * These methods convert the given type to a compatible type that SQLite supports and vice versa.
 * ROOM database automatically knows which type converters must use.
 */
