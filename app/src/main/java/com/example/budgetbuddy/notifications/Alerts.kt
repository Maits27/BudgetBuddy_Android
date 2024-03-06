package com.example.budgetbuddy.notifications

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.budgetbuddy.Data.Tema
import com.example.budgetbuddy.Data.obtenerTema
import com.example.budgetbuddy.R
import com.example.budgetbuddy.utils.AppLanguage

@Composable
fun Informacion(show: Boolean, onConfirm: () -> Unit) {
    val context = LocalContext.current
    val shareMessage = stringResource(id = R.string.share_message)
    val asunto = stringResource(id = R.string.asunto)
    val contenidoMail = stringResource(id = R.string.contenidoEmail)
    if(show){
        AlertDialog(
            onDismissRequest = {},
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = {
                Row {
                    TextButton(onClick = {
                        compartirContenido(context, shareMessage)
                        onConfirm()
                    }
                    ) {
                        Text(text =  stringResource(id = R.string.share))
                    }
                    TextButton(onClick = {
                        compartirContenido(context, contenidoMail, asunto = asunto)
                        onConfirm()
                    }
                    ) {
                        Text(text =  "Email")
                    }
                }
            }, dismissButton = {
                TextButton(onClick = { onConfirm() }
                ) {
                    Text(text = stringResource(id = R.string.ok))
                }
            },
            title = { Text(text = stringResource(id = R.string.app_name)) },
            text = {
                Text(text = stringResource(id = R.string.app_description))
            }
        )
    }
}

@Composable
fun Idiomas(
    show: Boolean,
    onLanguageChange:(AppLanguage)->Unit,
    onConfirm: () -> Unit
) {
    if(show){
        AlertDialog(
            onDismissRequest = {},
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = { TextButton(onClick = { onConfirm() }){
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.change_lang)) },
            text = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    for (idioma in AppLanguage.entries){
                        Button(
                            onClick = {
                                onConfirm()
                                onLanguageChange(AppLanguage.getFromCode(idioma.code))},
                            Modifier.fillMaxWidth()
                        ) {
                            Text(text = idioma.language)
                        }
                    }
                }
            }
        )
    }
}
@Composable
fun Temas(
    show: Boolean,
    idioma:String,
    onThemeChange:(Int)->Unit,
    onConfirm: () -> Unit
) {
    if(show){
        AlertDialog(
            onDismissRequest = {},
            containerColor = MaterialTheme.colorScheme.background,
            confirmButton = { TextButton(onClick = { onConfirm() }){
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.change_theme)) },
            text = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Button(
                        onClick = {
                            onThemeChange(0)
                            onConfirm()
                            },
                        Modifier.fillMaxWidth()
                    ) {
                        Text(text = obtenerTema(Tema.Verde, idioma))
                    }
                    Button(
                        onClick = {
                            onThemeChange(1)
                            onConfirm()
                            },
                        Modifier.fillMaxWidth()
                    ) {
                        Text(text = obtenerTema(Tema.Azul, idioma))
                    }
                    Button(
                        onClick = {
                            onThemeChange(2)
                            onConfirm()
                        },
                        Modifier.fillMaxWidth()
                    ) {
                        Text(text = obtenerTema(Tema.Morado, idioma))
                    }
                }
            }
        )
    }
}

@Composable
fun ErrorAlert(show: Boolean, mensaje: String, onConfirm: () -> Unit) {
    Log.d("DOWNLOAD ERROR", "ERROR ALERT: $show")
    if(show){
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.error), color = MaterialTheme.colorScheme.onError) },
            text = {
                Text(text = mensaje, color = Color.Black)
            }
        )
    }
}

