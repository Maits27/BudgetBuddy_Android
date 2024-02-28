package com.example.budgetbuddy.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.ErrorDeInsert
import com.example.budgetbuddy.R
import java.time.LocalDate
import java.time.format.DateTimeParseException

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Add(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){
    var nombre by rememberSaveable { mutableStateOf("") }
    var euros by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var error_message by remember { mutableStateOf("") }

    var isTextFieldFocused by remember { mutableStateOf(-1) }
    var showError by rememberSaveable { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = stringResource(id = R.string.add_element),
            Modifier.padding(16.dp)
        )
        Divider()

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(id = R.string.name_element)) },
            keyboardActions = KeyboardActions(
                onDone = {
                    isTextFieldFocused = -1
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    if(it.isFocused){
                        isTextFieldFocused = 0
                    }
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
                    isTextFieldFocused = -1
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    if(it.isFocused){
                        isTextFieldFocused = 1
                    }
                }
        )
        OutlinedTextField(
            value = "",
            onValueChange = {
                fecha = try {
                    LocalDate.parse(it)
                } catch (e: DateTimeParseException) {
                    // Manejo de errores o asigna un valor predeterminado
                    LocalDate.now()
                }
            },
            label = { Text(stringResource(id = R.string.date_pick)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    isTextFieldFocused = -1
                    keyboardController?.hide()
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .onFocusChanged {
                    if(it.isFocused){
                        isTextFieldFocused = 2
                    }
                }
        )


        val error_double = stringResource(id = R.string.error_double)
        val error_insert = stringResource(id = R.string.error_insert)

        Button(
            onClick = {
                if (nombre!="" && euros!=""){
                    if (euros.toDoubleOrNull() != null){
                        appViewModel.añadirGasto(nombre, euros.toDouble(), fecha)
                    }else{
                        showError = true
                        error_message = error_double
                    }
                }else{
                    showError = true
                    error_message = error_insert
                }
                if(!showError){
                    navController.navigateUp()
                }
            },
            Modifier
                .padding(8.dp, 16.dp)
        ) {
            Text(text = stringResource(id = R.string.add))
        }

        ErrorDeInsert(show = showError, mensaje = error_message) { showError = false }
    }
}
