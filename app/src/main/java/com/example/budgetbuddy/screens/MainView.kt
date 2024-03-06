package com.example.budgetbuddy2.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.VM.AppViewModel
import com.example.budgetbuddy.Data.Diseño
import com.example.budgetbuddy.Data.Gasto
import com.example.budgetbuddy.Data.TipoGasto
import com.example.budgetbuddy.VM.PreferencesViewModel
import com.example.budgetbuddy.notifications.Idiomas
import com.example.budgetbuddy.notifications.Informacion
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens
import com.example.budgetbuddy.notifications.ErrorAlert
import com.example.budgetbuddy.notifications.Temas
import com.example.budgetbuddy.notifications.compartirContenido
import com.example.budgetbuddy.notifications.downloadNotification
import com.example.budgetbuddy.screens.Add
import com.example.budgetbuddy.screens.Dashboards
import com.example.budgetbuddy.screens.Edit
import com.example.budgetbuddy.screens.Home
import com.example.budgetbuddy.utils.AppLanguage
import com.example.budgetbuddy.utils.toLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    appViewModel: AppViewModel,
    preferencesViewModel: PreferencesViewModel
){
    val primero by preferencesViewModel.primero.collectAsState(initial = false)
    if(primero){
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch(Dispatchers.IO) {appViewModel.gastosPrueba()}
        preferencesViewModel.primero()
    }
    val context = LocalContext.current
    val navController = rememberNavController()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val isVertical = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    val idioma by preferencesViewModel.idioma.collectAsState(initial = preferencesViewModel.currentSetLang)
    val fecha  by appViewModel.fecha.collectAsState(initial = LocalDate.now())
    val factura by appViewModel.facturaActual(fecha, idioma).collectAsState(initial = "")
    val total  by appViewModel.totalGasto(fecha).collectAsState(initial = 0.0)

    var showDownloadError by rememberSaveable { mutableStateOf(false) }
    var showExpansion by rememberSaveable { mutableStateOf(false) }
    var gastoEditable by remember { mutableStateOf(Gasto("", 0.0, fecha, TipoGasto.Otros)) }

    val factura_init = stringResource(id = R.string.factura_init, fecha.toString())
    val factura_end = stringResource(id = R.string.factura_total, total.toString())
    val tit_notificacion = stringResource(id = R.string.factura_download)
    val desk_notificacion = stringResource(id = R.string.download_description, fecha.toString())

    val onClose:()->Unit = {showExpansion = false}

    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            if (navBackStackEntry?.destination?.route == AppScreens.Facturas.route) {
                if (factura!=""){
                    if(!showExpansion){
                        FloatButton(
                            painterResource(id = R.drawable.add)
                        ) {
                            showExpansion = true
                        }
                    }
                    val texto_factura = "$factura_init$factura\n$factura_end"
                    Expansion(showExpansion, texto_factura, onClose){
                        showDownloadError = !appViewModel.guardarDatosEnArchivo(fecha, texto_factura)
                        if (!showDownloadError){
                            downloadNotification(
                                context = context,
                                titulo = tit_notificacion,
                                description = desk_notificacion,
                                id = fecha.toLong().toInt()
                            )
                        }
                        showExpansion = false
                    }
                    ErrorAlert(show = showDownloadError, mensaje = stringResource(id = R.string.download_error)) {
                        showDownloadError = false
                    }
                }
            } else if (navBackStackEntry?.destination?.route == AppScreens.Home.route) {
                FloatButton(
                    painterResource(id = R.drawable.add)
                ) {
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
            TopBarMainView(
                navController = navController,
                preferencesViewModel = preferencesViewModel,
                idioma = idioma
            )
        },
        bottomBar = {
            if(isVertical){
                BottomBarMainView(
                    navController = navController
                )
            }
        }
    ){ innerPadding ->
        if (!isVertical){
            NavHorizontal(idioma, gastoEditable, innerPadding, navController, appViewModel)

        }else{
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = AppScreens.Home.route
            ) {
                composable(AppScreens.Home.route) { Home(appViewModel, idioma, navController){gastoEditable = it} }
                composable(AppScreens.Add.route){ Add(appViewModel, navController, idioma.code)}
                composable(AppScreens.Edit.route){ Edit(gastoEditable, appViewModel, navController, idioma.code)}
                composable( AppScreens.Facturas.route) { Facturas(appViewModel, idioma) }
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarMainView(
    navController: NavController,
    preferencesViewModel: PreferencesViewModel,
    idioma: AppLanguage
){

    var showInfo by rememberSaveable { mutableStateOf(false) }
    var showLang by rememberSaveable { mutableStateOf(false) }
    var showTheme by rememberSaveable { mutableStateOf(false) }

    val onLanguageChange:(AppLanguage)-> Unit = {
        preferencesViewModel.changeLang(it)
    }
    val onThemeChange:(Int)-> Unit = {
        preferencesViewModel.changeTheme(it)
    }
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
            IconButton( onClick = { showTheme = true } ){
                Icon(
                    painterResource(id = R.drawable.palette),//Icons.Filled.Settings,
                    contentDescription = stringResource(id = R.string.infor),
                    tint = Color.White
                )
            }
        },
    )
    Informacion(showInfo) { showInfo = false }
    Idiomas(showLang, onLanguageChange){ showLang = false }
    Temas(showTheme, idioma.code ,onThemeChange){ showTheme = false }

}

@Composable
fun BottomBarMainView(
    navController: NavController
){
    BottomNavigation (
        backgroundColor = MaterialTheme.colorScheme.secondary
    ){
        val items = listOf(
            Diseño(AppScreens.Facturas, "Factura", painterResource(id = R.drawable.bill)),
            Diseño(AppScreens.Home, "Home", painterResource(id = R.drawable.home)),
            Diseño(AppScreens.Dashboards, "Metrics", painterResource(id = R.drawable.dashboard)),
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            BottomNavigationItem(
                selectedContentColor = MaterialTheme.colorScheme.background,
                icon = { Icon(screen.icono, contentDescription = null, tint = Color.White) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.pantalla.route } == true,
                onClick = {
                    navController.navigate(screen.pantalla.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

    }
}
@Composable
fun NavHorizontal(idioma: AppLanguage, gasto:Gasto, innerPadding: PaddingValues, navController:NavHostController, appViewModel: AppViewModel){
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
            composable(AppScreens.Home.route) { Home(appViewModel, idioma, navController){gastoEditable = it} }
            composable(AppScreens.Add.route) { Add(appViewModel, navController, idioma.code) }
            composable(AppScreens.Edit.route) { Edit(gastoEditable, appViewModel, navController, idioma.code) }
            composable(AppScreens.Facturas.route) { Facturas(appViewModel, idioma ) }
            composable(AppScreens.Dashboards.route) { Dashboards(appViewModel, idioma.code) }
        }
    }
}

@Composable
fun Expansion(show: Boolean, textoFactura: String, onClose:()-> Unit, onDownload:()-> Unit){
    if(show){
        val context = LocalContext.current
        Column (
            horizontalAlignment = Alignment.End
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.download), Modifier.padding(end = 10.dp))
                FloatButton(
                    painterResource(id = R.drawable.download)
                ) {
                    onDownload()
                }
            }
            Row (
                Modifier.padding(top = 5.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = stringResource(id = R.string.share), Modifier.padding(end=10.dp))
                FloatButton(
                    painterResource(id = R.drawable.send)
                ) {
                    compartirContenido(context = context, textoFactura)
                    onClose()
                }
            }

            FloatButton(
                painterResource(id = R.drawable.close)
            ) {
                onClose()
            }
        }
    }
}