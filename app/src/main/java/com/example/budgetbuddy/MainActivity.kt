package com.example.budgetbuddy

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.budgetbuddy.VM.AppViewModel
import com.example.budgetbuddy.VM.PreferencesViewModel
import com.example.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.example.budgetbuddy2.screens.MainView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val appViewModel by viewModels<AppViewModel> ()
    val preferencesViewModel by viewModels<PreferencesViewModel> ()
    companion object{
        const val CHANNEL_ID = "BudgetBuddy"
    }


    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BudgetBuddy", "Init")
        super.onCreate(savedInstanceState)
        Log.d("BudgetBuddy", "Notificaciones")
        createNotificationChannel()


        setContent {
            BudgetBuddyTheme(preferencesViewModel = preferencesViewModel) {
                // A surface container using the 'background' color from the theme
                val guardarFichero: ( LocalDate, String)-> Boolean = { fecha, datos -> guardarDatosEnArchivo(appViewModel, fecha, datos) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    NotificationPermission()
                    MainView(
                        appViewModel = appViewModel,
                        preferencesViewModel = preferencesViewModel,
                        guardarFichero
                    )
                }
            }
        }

    }
    fun guardarDatosEnArchivo(appViewModel: AppViewModel, fecha: LocalDate, datos:String): Boolean{
        val nombre = appViewModel.fecha_txt(fecha)
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 23
        )

        // getExternalStoragePublicDirectory() represents root of external storage, we are using DOWNLOADS
        // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
        val folder: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Storing the data in file with name as geeksData.txt
        val file = File(folder, "Factura${nombre}.txt")
        return writeTextData(file, datos)
    }
    fun writeTextData(file: File, data: String):Boolean {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(data.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                }
            }
        }
        return true
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

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun NotificationPermission(){
        val permissionState = rememberPermissionState(
            permission = android.Manifest.permission.POST_NOTIFICATIONS
        )
        LaunchedEffect(true){
            permissionState.launchPermissionRequest()
        }
    }

}
fun getAPILevel(): Int {
    return android.os.Build.VERSION.SDK_INT
}
