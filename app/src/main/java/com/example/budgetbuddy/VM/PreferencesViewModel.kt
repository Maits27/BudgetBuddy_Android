package com.example.budgetbuddy.VM

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbuddy.Data.Enumeration.AppLanguage
import com.example.budgetbuddy.preferences.IGeneralPreferences
import com.example.budgetbuddy.utils.LanguageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: IGeneralPreferences,
    private val languageManager: LanguageManager,
) : ViewModel() {


    /*------------------------------------------------
    |               Preferences States               |
    ------------------------------------------------*/

    // Current app's language and preferred language (may not be the same at the beginning)
    val currentSetLang by languageManager::currentLang
    val idioma = preferencesRepository.language().map { AppLanguage.getFromCode(it) }

    val theme = preferencesRepository.getThemePreference()

    var primero = preferencesRepository.getPrimero()

    /*************************************************
     **                    Events                   **
     *************************************************/


    //------------   Language Related   ------------//

    // Change language preference, adjust the locale and reload de interface
    fun changeLang(idioma: AppLanguage) {
        languageManager.changeLang(idioma)
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setLanguage(idioma.code) }
    }

    fun changeTheme(color: Int){
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.saveThemePreference(color) }
    }

    fun primero(){
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.primero() }
    }

//    fun reloadLang(lang: AppLanguage, context: Context) = languageManager.changeLang(lang)

    //------------   Theme Related   ------------//

}
