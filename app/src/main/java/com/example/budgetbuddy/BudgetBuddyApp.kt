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

        /*------------------------------------------------
        |          Create Notification Channels          |
        ------------------------------------------------*/

        // Create the Authentication Notification Channel

//        val channel = NotificationChannel(
//            MainActivity.CHANNEL_ID,
//            getString(R.string.channel_name),
//            NotificationManager.IMPORTANCE_HIGH
//        ).apply {
//            description = getString(R.string.channel_description)
//        }
//
//        // Register the channel with the system.
//        val notificationManager: NotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
    }
}


/*******************************************************************************
 ****                    Enum Class for Notification IDs                    ****
 *******************************************************************************/

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermission(){
//    val permissionState = rememberPermissionState(
//        permission = Manifest.permission.POST_NOTIFICATIONS)
//    LaunchedEffect(true){
//        permissionState.launchPermissionRequest()
//    }

}
/*
Class used to centralize and have better control over application's notification IDs.
It uses an enum class what gives better readability over the code, and avoids ID mistakes

(In this app we only had one notification, but it's a good practice and eases future expansions and technical debt)
*/

enum class NotificationID(val id: Int) {
    CLOSE_APP(1)
}