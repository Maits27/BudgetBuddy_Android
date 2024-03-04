package com.example.budgetbuddy

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbuddy.preferences.IGeneralPreferences
import com.example.budgetbuddy.utils.AppLanguage
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

    /*************************************************
     **                    Events                   **
     *************************************************/


    //------------   Language Related   ------------//

    // Change language preference, adjust the locale and reload de interface
    fun changeLang(idioma: AppLanguage, context: Context) {
        languageManager.changeLang(idioma, context)
        viewModelScope.launch(Dispatchers.IO) { preferencesRepository.setLanguage(idioma.code) }
    }

    fun reloadLang(lang: AppLanguage, context: Context) = languageManager.changeLang(lang, context, false)

}
