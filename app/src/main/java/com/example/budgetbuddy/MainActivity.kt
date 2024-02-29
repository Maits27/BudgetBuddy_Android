package com.example.budgetbuddy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.navigation.AppScreens
import com.example.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.example.budgetbuddy2.screens.MainView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Locale

class MainActivity : ComponentActivity() {

    val appViewModel by viewModels<AppViewModel>()
    companion object{
        const val CHANNEL_ID = "BudgetBuddy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BudgetBuddy", "Init")
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        Log.d("BudgetBuddy", "Notificaciones")
        setContent {
            BudgetBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationPermission()
                    MainView(
                        appViewModel = appViewModel,
                        modifier = Modifier,
                    ){code: String -> cambiarIdioma(code)}
                }
            }
        }

    }

    private fun cambiarIdioma(codigo: String){
        resources.configuration.setLocale(Locale(codigo))
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        resources.configuration.locale = Locale(codigo)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_description)
            }

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun NotificationPermission(){
        val permissionState = rememberPermissionState(
            permission = android.Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(true){
            permissionState.launchPermissionRequest()
    }
}

