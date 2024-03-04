package com.example.budgetbuddy2.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.Diseño
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.TipoGasto
import com.example.budgetbuddy.PreferencesViewModel
import com.example.budgetbuddy.screens.Idiomas
import com.example.budgetbuddy.screens.Informacion
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens
import com.example.budgetbuddy.screens.Add
import com.example.budgetbuddy.screens.Dashboards
import com.example.budgetbuddy.screens.Edit
import com.example.budgetbuddy.screens.Home
import com.example.budgetbuddy.utils.AppLanguage
import java.io.File
import java.io.FileWriter
import java.time.LocalDate

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    appViewModel: AppViewModel,
    preferencesViewModel: PreferencesViewModel
){
    val context = LocalContext.current
    val idioma by preferencesViewModel.idioma.collectAsState(initial = preferencesViewModel.currentSetLang)
    val onLanguageChange:(AppLanguage)-> Unit = {
        preferencesViewModel.changeLang(it, context)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showInfo by rememberSaveable { mutableStateOf(false) }
    var showLang by rememberSaveable { mutableStateOf(false) }
    var showDownloadError by rememberSaveable { mutableStateOf(false) }
    var showDownloadOk by rememberSaveable { mutableStateOf(false) }

    val navController = rememberNavController()
    val fecha  by appViewModel.fecha.collectAsState(initial = LocalDate.now())
    val factura by appViewModel.facturaActual(fecha).collectAsState(initial = "")

    var gastoEditable by remember { mutableStateOf(Gasto("", 0.0, fecha, TipoGasto.Otros)) }

    val configuration = LocalConfiguration.current
    val isVertical = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            if (navBackStackEntry?.destination?.route == AppScreens.Facturas.route) {
                if (factura!=""){
                    FloatButton( painterResource(id = R.drawable.download)) {
                        showDownloadOk = guardarDatosEnArchivo(factura, appViewModel.fecha_txt(fecha))
                        showDownloadError = !showDownloadOk
                    }
                }
            } else if (navBackStackEntry?.destination?.route == AppScreens.Home.route) {
                FloatButton( painterResource(id = R.drawable.add)) {
                    navController.navigate(AppScreens.Add.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton( onClick = { showInfo = true } ){
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = stringResource(id = R.string.infor),
                            tint = Color.White
                        )
                    }
                    IconButton( onClick = { showLang = true } ){
                        Icon(
                            painterResource(id = R.drawable.baseline_translate_24),//Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.infor),
                            tint = Color.White
                        )
                    }

                },
            )
            Informacion(showInfo) { showInfo = false }
            Idiomas(showLang, onLanguageChange){ showLang = false }
        },
        bottomBar = {
            if(isVertical){
                BottomNavigation (
                    backgroundColor = MaterialTheme.colorScheme.secondary
                ){
                    val items = listOf(
                        Diseño(AppScreens.Facturas, "Factura", painterResource(id = R.drawable.bill)),
                        Diseño(AppScreens.Home, "Home", painterResource(id = R.drawable.home)),
                        Diseño(AppScreens.Add, "Add", painterResource(id = R.drawable.add)),
                        Diseño(AppScreens.Dashboards, "Metrics", painterResource(id = R.drawable.dashboard)),
                    )

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            selectedContentColor = MaterialTheme.colorScheme.background,
                            icon = { Icon(screen.icono, contentDescription = null, tint = Color.White) },
//                            label = { Text(screen.nombre, color = Color.White) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.pantalla.route } == true,
                            onClick = {
                                navController.navigate(screen.pantalla.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }

                }
            }
        }
    ){ innerPadding ->
        if (!isVertical){
            NavHorizontal(idioma.code, gastoEditable, innerPadding, navController, appViewModel)

        }else{
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = AppScreens.Home.route
            ) {
                composable(AppScreens.Home.route) { Home(appViewModel, navController){gastoEditable = it} }
                composable(AppScreens.Add.route){ Add(appViewModel, navController, idioma.code)}
                composable(AppScreens.Edit.route){ Edit(gastoEditable, appViewModel, navController, idioma.code)}
                composable( AppScreens.Facturas.route) { Facturas(appViewModel) }
                composable( AppScreens.Dashboards.route) { Dashboards(appViewModel, idioma.code) }
            }
        }
        
    }
}

@Composable
fun FloatButton(icon: Painter, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondary,
        shape = CircleShape
    ) {
        Icon(icon, stringResource(id = R.string.add))
    }
}

@Composable
fun NavHorizontal(idioma: String, gasto:Gasto, innerPadding: PaddingValues, navController:NavHostController, appViewModel: AppViewModel){
    var gastoEditable = gasto
    Row {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .fillMaxHeight()
                .padding(innerPadding),
        ) {
            val items = listOf(
                Diseño(AppScreens.Home, "Home", painterResource(id = R.drawable.home)),
                Diseño(AppScreens.Add, "Add", painterResource(id = R.drawable.add)),
                Diseño(AppScreens.Facturas, "Facturas", painterResource(id = R.drawable.bill)),
                Diseño(AppScreens.Dashboards, "Dashboards", painterResource(id = R.drawable.dashboard)),
            )
            items.forEach { screen ->
                Button(
                    modifier = Modifier.padding(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = {
                        navController.navigate(screen.pantalla.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ) {
                    Icon(screen.icono, contentDescription = null, tint = Color.White)
//                    Text(screen.nombre, color = Color.White)
                }
            }

        }
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.Home.route
        ) {
            composable(AppScreens.Home.route) { Home(appViewModel, navController){gastoEditable = it} }
            composable(AppScreens.Add.route) { Add(appViewModel, navController, idioma) }
            composable(AppScreens.Edit.route) { Edit(gastoEditable, appViewModel, navController, idioma) }
            composable(AppScreens.Facturas.route) { Facturas(appViewModel) }
            composable(AppScreens.Dashboards.route) { Dashboards(appViewModel, idioma) }
        }
    }
}
private fun guardarDatosEnArchivo(datos: String, nombre: String): Boolean {
    val estadoAlmacenamientoExterno = Environment.getExternalStorageState()
    if (estadoAlmacenamientoExterno == Environment.MEDIA_MOUNTED) {
        val directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val archivo = File(directorioDescargas, "Factura${nombre}.txt")


        FileWriter(archivo).use { writer ->
            writer.append(datos)
        }
        Log.d("Download","Download")
    }
    return true
}
