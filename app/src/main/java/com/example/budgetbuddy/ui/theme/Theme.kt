package com.example.budgetbuddy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.budgetbuddy.VM.PreferencesViewModel

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xff082e20), //Verde oscuro
    secondary = Color(0xff082e20), //Verde oscuro
    tertiary = Color(0xffCFFFDB), //Verde claro
    background = Color(0xffF4F4F4), //Gris claro
    onPrimary = Color(0xffF4F4F4), //Gris claro
    onSecondary = Color(0xffF4F4F4), //Gris claro
    onTertiary = Color(0xff082e20), //Verde oscuro
    primaryContainer = Color(0xff082e20), //Verde oscuro
    onError = Color(0xff980722),
    onBackground = Color.DarkGray,
    onPrimaryContainer = Color(0xffF4F4F4), //Gris claro
    onSecondaryContainer = Color(0xff082e20), //Verde oscuro
    onErrorContainer =  Color(0xffCFFFDB),
    surface = Color.White,
    onSurface = Color(0xff082e20), //Verde oscuro
    onSurfaceVariant = Color(0xff082e20), //Verde oscuro
    inverseOnSurface = Color(0xff082e20), //Verde oscuro
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xff0E2D68), //Azul oscuro
    secondary = Color(0xff0E2D68), //Azul oscuro
    tertiary = Color(0xffCFE4FF), //Azul claro
    background = Color(0xffF4F4F4), //Gris claro
    onPrimary = Color(0xffF4F4F4), //Gris claro
    onSecondary = Color(0xffF4F4F4), //Gris claro
    onTertiary = Color(0xff0E347D), //Azul oscuro
    primaryContainer = Color(0xff0E2D68), //Azul oscuro
    onError = Color(0xff980722),
    onBackground = Color.DarkGray,
    onPrimaryContainer = Color(0xffF4F4F4), //Gris claro
    onSecondaryContainer = Color(0xff0E2D68), //Azul oscuro
)

private val CustomColorScheme = darkColorScheme(
    primary = Color(0xff4A126E), // Morado oscuro
    secondary = Color(0xff4A126E), // Morado oscuro
    tertiary = Color(0xffEBCFFF), // Rosa claro
    background = Color(0xffF4F4F4), // Gris claro
    onPrimary = Color(0xffF4F4F4), // Gris claro
    onSecondary = Color(0xffF4F4F4), // Gris claro
    onTertiary = Color(0xff4A126E), // Morado oscuro
    primaryContainer = Color(0xff4A126E), // Morado oscuro
    onError = Color(0xff980722),
    onBackground = Color.DarkGray,
    onPrimaryContainer = Color(0xffF4F4F4), // Gris claro
    onSecondaryContainer = Color(0xff4A126E), // Morado oscuro
    onErrorContainer =  Color(0xffFFC0CB),
    surface = Color.White,
    onSurface = Color(0xff4A126E),// Morado oscuro
    onSurfaceVariant = Color(0xff4A126E),// Morado oscuro
    inverseOnSurface = Color(0xff4A126E)// Morado oscuro
)


@Composable
fun BudgetBuddyTheme(
    preferencesViewModel: PreferencesViewModel,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val dark by preferencesViewModel.theme.collectAsState(initial = true)
    if (dark==0){
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography,
            content = content
        )
    }else if (dark==1){
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }else{
        MaterialTheme(
            colorScheme = CustomColorScheme,
            typography = Typography,
            content = content
        )
    }

}