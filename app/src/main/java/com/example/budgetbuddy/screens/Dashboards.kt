package com.example.budgetbuddy.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.ExpenseFilter
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens

@Composable
fun Dashboards(appViewModel: AppViewModel, navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(id = R.string.factura_total, appViewModel.gastoTotal().toString()))
            DropdownMenu(
                expanded = false,
                onDismissRequest = { /* No action on dismiss */ },
                modifier = Modifier.padding(end = 16.dp)
            ) {

            }
        }


        // Additional content like a list of expenses, additional details, etc.
    }
}

@Composable
fun ExpenseChart(
    gastos: List<Gasto>,
    filter: ExpenseFilter
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Draw your expense chart here based on the provided expenses and filter
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                // Implement your drawing logic here using the Canvas API
            }
        )
    }
}