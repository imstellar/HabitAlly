package com.assignment.habitally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.assignment.habitally.data.AppState
import com.assignment.habitally.ui.theme.HabitAllyTheme

sealed class AppScreens(val route: String, val label: String, val icon: ImageVector) {
    object Home: AppScreens("home", "Home", Icons.Filled.Home)
    object Targets: AppScreens("targets", "Targets", Icons.Filled.Edit)
}

val bottomNavItems = listOf(
    AppScreens.Home,
    AppScreens.Targets
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppController()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppController() {
    val navController = rememberNavController()
    var currentScreen by remember {mutableStateOf("")}
    val appState: AppState = viewModel()

    HabitAllyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                    ),
                    title = {
                        Column {
                            Text("Welcome to Habitally")
                            Text(
                                if (currentScreen == "targets") {"Get started by tracking your habits below"} else {"Change your targets to better fit your goals"},
                                modifier = Modifier.padding(top = 4.dp), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavItems.forEach {screen ->
                        NavigationBarItem(
                            icon = {Icon(screen.icon, contentDescription = screen.label)},
                            label = {Text(screen.label, style = MaterialTheme.typography.titleMedium)},
                            selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // re-selecting the same item
                                    launchSingleTop = true
                                    // Restore state when re-selecting a previously selected item
                                    restoreState = true
                                }
                                currentScreen = currentDestination?.route.toString()
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreens.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreens.Home.route) {
                    AppHome(
                        appState = appState,
                        onNavigate = {
                            currentScreen = "home"
                            navController.navigate(AppScreens.Targets.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }}
                    )}
                composable(AppScreens.Targets.route) {AppTargets(appState = appState)}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppController()
}