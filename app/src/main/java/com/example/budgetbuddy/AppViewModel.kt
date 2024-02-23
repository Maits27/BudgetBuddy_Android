package com.example.budgetbuddy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {

    var id by mutableStateOf(0)

}