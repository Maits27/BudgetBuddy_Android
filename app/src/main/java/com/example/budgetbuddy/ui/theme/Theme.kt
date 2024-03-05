package com.example.budgetbuddy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.core.view.WindowCompat
import com.example.budgetbuddy.PreferencesViewModel
import com.example.budgetbuddy.R

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
    surface = Color(0xffF4F4F4), //Gris claro
    onSurface = Color.DarkGray,
    onSurfaceVariant = Color.DarkGray,
    inverseOnSurface = Color.DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xff08152E), //Azul oscuro
    secondary = Color(0xff08152E), //Azul oscuro
    tertiary = Color(0xffCFE4FF), //Azul claro
    background = Color(0xffF4F4F4), //Gris claro
    onPrimary = Color(0xffF4F4F4), //Gris claro
    onSecondary = Color(0xffF4F4F4), //Gris claro
    onTertiary = Color(0xff08152E), //Azul oscuro
    primaryContainer = Color(0xff08152E), //Azul oscuro
    onError = Color(0xff980722),
    onBackground = Color.DarkGray,
    onPrimaryContainer = Color(0xffF4F4F4), //Gris claro
    onSecondaryContainer = Color(0xff08152E), //Azul oscuro
)

private val CustomColorScheme = darkColorScheme(
    primary = Color(0xff1F082E), // Morado oscuro
    secondary = Color(0xff1F082E), // Morado oscuro
    tertiary = Color(0xffEBCFFF), // Rosa claro
    background = Color(0xffF4F4F4), // Gris claro
    onPrimary = Color(0xffF4F4F4), // Gris claro
    onSecondary = Color(0xffF4F4F4), // Gris claro
    onTertiary = Color(0xff1F082E), // Morado oscuro
    primaryContainer = Color(0xff1F082E), // Morado oscuro
    onError = Color(0xff980722),
    onBackground = Color.DarkGray,
    onPrimaryContainer = Color(0xffF4F4F4), // Gris claro
    onSecondaryContainer = Color(0xff1F082E), // Morado oscuro
    onErrorContainer =  Color(0xffFFC0CB),
    surface = Color(0xffF4F4F4), // Gris claro
    onSurface = Color.DarkGray,
    onSurfaceVariant = Color.DarkGray,
    inverseOnSurface = Color.DarkGray
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