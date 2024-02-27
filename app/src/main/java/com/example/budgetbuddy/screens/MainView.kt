package com.example.budgetbuddy2.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.AppViewModel
import com.example.budgetbuddy.Data.Diseño
import com.example.budgetbuddy.Idiomas
import com.example.budgetbuddy.Informacion
import com.example.budgetbuddy.R
import com.example.budgetbuddy.navigation.AppScreens
import com.example.budgetbuddy.screens.Add
import com.example.budgetbuddy.screens.AddButton
import com.example.budgetbuddy.screens.Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    appViewModel: AppViewModel,
    modifier: Modifier,
    cambiarIdioma:(String) -> Unit
){
    cambiarIdioma(appViewModel.idioma.code)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var showInfo by rememberSaveable { mutableStateOf(false) }
    var showLang by rememberSaveable { mutableStateOf(false) }
    var showAdd by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()
    Scaffold (
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            AddButton{showAdd = true}
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.tertiary
                ),
//                navigationIcon = {
//                    IconButton(onClick = {  }) {
//                        Icon(
//                            imageVector = Icons.Filled.ArrowBack,
//                            contentDescription = stringResource(id = R.string.back),
//                            tint = Color.White
//                        )
//                    }
//                },
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
                            Icons.Filled.Settings,
                            contentDescription = stringResource(id = R.string.infor),
                            tint = Color.White
                        )
                    }

                },
                scrollBehavior = scrollBehavior,
            )
            Informacion(showInfo) { showInfo = false }
            Idiomas(showLang, appViewModel){ showLang = false }
        },
        bottomBar = {
            BottomNavigation (
                backgroundColor = MaterialTheme.colorScheme.secondary
            ){
                val items = listOf(
                    Diseño(AppScreens.Facturas, "Facturas", Icons.Filled.ShoppingCart),
                    Diseño(AppScreens.Home, "Home", Icons.Filled.Home),
                    Diseño(AppScreens.Add, "Add", Icons.Filled.Add),
                )

                BottomNavigation (
                    backgroundColor = MaterialTheme.colorScheme.secondary
                ){
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->
                        BottomNavigationItem(
                            selectedContentColor = MaterialTheme.colorScheme.background,
                            icon = { Icon(screen.icono, contentDescription = null, tint = Color.White) },
                            label = { Text(screen.nombre, color = Color.White) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.pantalla.route } == true,
                            onClick = {
                                navController.navigate(screen.pantalla.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
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
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = AppScreens.Home.route
        ) {
            composable(AppScreens.Home.route) { Home(innerPadding, appViewModel, navController) }
            composable( AppScreens.Add.route) { Add(innerPadding, appViewModel, navController) }
            composable( AppScreens.Facturas.route) { Facturas(innerPadding, appViewModel, navController) }
        }
    }
}