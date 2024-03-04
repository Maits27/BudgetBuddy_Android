package com.example.budgetbuddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.budgetbuddy.ui.theme.BudgetBuddyTheme
import com.example.budgetbuddy2.screens.MainView
import dagger.hilt.android.AndroidEntryPoint

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

        setContent {
            preferencesViewModel.reloadLang(preferencesViewModel.idioma.collectAsState(initial = preferencesViewModel.currentSetLang).value, this)
            BudgetBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val coroutineScope = rememberCoroutineScope()
                    MainView(
                        appViewModel = appViewModel,
                        preferencesViewModel = preferencesViewModel
                    )
                }
            }
        }

    }




}
