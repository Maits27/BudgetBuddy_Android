package com.example.budgetbuddy.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.budgetbuddy.MainActivity
import com.example.budgetbuddy.R

fun downloadNotification(context: Context,
                         titulo: String,
//                         opcion1:String,
//                         opcion2: String,
                         description: String,
                         id: Int
){
    val notificationManager = context.getSystemService(NotificationManager::class.java)
//    val requestCodeCerrarApp = 1
//    val intent = Intent(context, MainActivity::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//    }
//    val reiniciarIntent: PendingIntent = PendingIntent.getActivity(context, requestCodeCerrarApp, intent, PendingIntent.FLAG_IMMUTABLE)
//
//    val intent2 = Intent(context, CerrarAppReceiver::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//    }
//    val cerrarAppIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_IMMUTABLE)

    var notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
        .setContentTitle(titulo)
        .setContentText(description)
        .setSmallIcon(R.drawable.download)
        .setAutoCancel(true)
        .setStyle(
            NotificationCompat.BigTextStyle()
            .bigText(description))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        // Agregar acciones (botones)
//        .addAction(R.drawable.ic_abrir_otra_parte, opcion1, reiniciarIntent)
//        .addAction(R.drawable.ic_cerrar_app, opcion2, cerrarAppIntent)
        .build()

    notificationManager.notify(id, notification)
}