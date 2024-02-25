package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SegundaPantalla(navController: NavController, appViewModel: AppViewModel, modifier: Modifier){
    var nombre by remember { mutableStateOf("") }
    var euros by remember { mutableStateOf("") }
    var error_message by remember { mutableStateOf("") }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var isTextFieldFocused2 by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showError by rememberSaveable { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(text = stringResource(id = R.string.add_element), Modifier.padding(16.dp))
        Divider()

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(id = R.string.name_element)) },
            keyboardActions = KeyboardActions(
                onDone = {
                    isTextFieldFocused = false
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    isTextFieldFocused = it.isFocused
                }
        )
        Divider()
        OutlinedTextField(
            value = euros,
            onValueChange = { euros = it },
            label = { Text(stringResource(id = R.string.price_element)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    isTextFieldFocused2 = false
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    isTextFieldFocused2 = it.isFocused
                }
        )

        val error_double = stringResource(id = R.string.error_double)
        val error_insert = stringResource(id = R.string.error_insert)

        Button(
            onClick = {
                if (nombre!="" && euros!=""){
                    if (euros.toDoubleOrNull() != null){
                        appViewModel.añadrirGasto(nombre, euros.toDouble())
                        Log.d("Segunda pantalla", appViewModel.listadoGastos.toString())
                    }else{
                        showError = true
                        error_message = error_double
                    }
                }else{
                    showError = true
                    error_message = error_insert
                }
            },
            Modifier
                .padding(8.dp, 16.dp)
        ) {
            Text(text = stringResource(id = R.string.add))
        }
        Button(
            onClick = {
                navController.navigateUp()
            },
            Modifier
                .padding(8.dp, 16.dp)
        ) {
            Text(text = stringResource(id = R.string.back))
        }
        ErrorDeInsert(show = showError, mensaje = error_message) { showError = false }
    }
}


@Composable
fun ErrorDeInsert(show: Boolean, mensaje: String, onConfirm: () -> Unit) {
    if(show){
        AlertDialog(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            onDismissRequest = {},
            confirmButton = { TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(id = R.string.ok))
            }
            },
            title = { Text(text = stringResource(id = R.string.error), color = MaterialTheme.colorScheme.onError) },
            text = {
                Text(text = mensaje, color = MaterialTheme.colorScheme.onPrimary)
            }
        )
    }
}