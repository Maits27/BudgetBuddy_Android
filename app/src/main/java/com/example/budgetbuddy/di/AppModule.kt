package com.example.budgetbuddy.di

import android.content.Context
import androidx.room.Room
import com.example.budgetbuddy.Data.Database
import com.example.budgetbuddy.Data.GastoDao
import com.example.budgetbuddy.Data.GastoRepository
import com.example.budgetbuddy.Data.IGastoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/*******************************************************************************
 ****                              Hilt Module                              ****
 *******************************************************************************/

/**
 *  This module is installed in [SingletonComponent], witch means,
 *  all the instance here are stored in the application level,
 *  so they will not be destroyed until application is finished/killed;
 *  and are shared between activities.
 *
 *  Hilt injects these instances in the required objects automatically.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // With Singleton we tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent (i.e. everywhere in the application)

    /*************************************************
     **           ROOM Database Instances           **
     *************************************************/
    @Singleton
    @Provides
    fun providesBudgetBuddyDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, Database::class.java, "database").build()

    //------------------   DAOs   ------------------//
    @Singleton
    @Provides
    fun provideGastoDao(db: Database) = db.gastoDao()


    /*************************************************
     **                 Repositories                **
     *************************************************/

    //-----------   Visits Repository   ------------//
    @Singleton
    @Provides
    fun provideGastoRepository(gastoDao: GastoDao): IGastoRepository = GastoRepository(gastoDao)


    //--   Settings & Preferences Repositories   ---//


//    @Singleton
//    @Provides
//    fun provideUserPreferences(@ApplicationContext app: Context): IUserPreferences = PreferencesRepository(app)


}