package com.example.budgetbuddy.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.budgetbuddy.R
import com.example.budgetbuddy.VM.AppViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


@Composable
fun Header(
    titulo: String,
    appViewModel: AppViewModel
){
    var showCalendar by remember { mutableStateOf(false) }
    val onCalendarConfirm: (LocalDate) -> Unit = {
        showCalendar = false
        appViewModel.cambiarFecha(it)
    }
    Text(
        text = titulo,
        Modifier.padding(top=16.dp, bottom = 10.dp)
    )
    Button(
        onClick = { showCalendar = true }
    ) {
        Text(text = stringResource(id = R.string.date_pick))
    }
    Calendario(show = showCalendar, onCalendarConfirm)
    Divider()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendario(show: Boolean, onConfirm: (LocalDate) -> Unit){
    if (show){
        val state = rememberDatePickerState()
        var date by remember { mutableStateOf(LocalDate.now()) }
        DatePickerDialog(
            onDismissRequest = { onConfirm(date) },
            confirmButton = {
                Button(onClick = { onConfirm(date) }) {
                    Text(text = stringResource(id = R.string.ok))
                }
            }
        ) {
            DatePicker(state = state)
            var time = state.selectedDateMillis?:System.currentTimeMillis()
            date = Instant.ofEpochMilli(time).atZone(ZoneId.of("UTC")).toLocalDate()
        }
    }
}

@Composable
fun NoData(){
    Column (
        modifier = Modifier
            .padding(vertical = 30.dp, horizontal = 10.dp)
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = stringResource(id = R.string.no_data))
    }
}
