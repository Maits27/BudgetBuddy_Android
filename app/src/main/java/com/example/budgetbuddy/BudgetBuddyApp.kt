package com.example.budgetbuddy

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.HiltAndroidApp



/*******************************************************************************
 ****                        Custom Aplication Class                        ****
 *******************************************************************************/

/*

This class is needed for Hilt Framework.

I also initialize notification channels here so they are not created each time we send a notification.

Avoiding code  repetition

*/

@HiltAndroidApp
class BudgetBuddyApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}


