package com.muamuathu.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.muamuathu.app.R
import com.muamuathu.app.nav.NavTarget
import com.muamuathu.app.nav.folder
import com.muamuathu.app.nav.note
import com.muamuathu.app.nav.todo
import com.muamuathu.app.ui.tab.BottomTabItem
import com.muamuathu.feature.consent.ConsentInfo
import com.muamuathu.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConsentInfo.init(this)
        setContent {
            MainApp()
        }
    }

    @Composable
    private fun MainApp() {
        AppTheme(useDarkTheme = false) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                GraphMainApp()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun GraphMainApp() {
        val navController = rememberNavController()

        val scaffoldState = rememberScrollState()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        var visibilityFloatButton by remember { mutableStateOf(true) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val tabs = remember { BottomTabItem.values() }
                AwareBottomBar(navController, tabs)
            },
        ) {
            Box(modifier = Modifier.padding(it)) {
                val data = navBackStackEntry?.destination?.route
                visibilityFloatButton =
                    NavTarget.Note.route == data || NavTarget.Folder.route == data || NavTarget.Todo.route == data

                NavGraph(navController)
            }
        }
    }


    @Composable
    fun AwareBottomBar(navController: NavController, tabs: Array<BottomTabItem>) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val routes = remember { tabs.map { it.route } }
        if (currentRoute in routes) {
            NavigationBar(containerColor = Color.White) {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(tab.icon),
                                contentDescription = null,
                                tint = if (currentRoute == tab.route) {
                                    colorResource(R.color.royal_blue)
                                } else {
                                    colorResource(R.color.mischka)
                                }
                            )
                        },
                        label = { Text(text = stringResource(tab.title)) },
                        selected = currentRoute == tab.route,
                        onClick = {
                            if (tab.route != currentRoute) {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(navController, NavTarget.GraphNote.route) {

//            navigation(
//                startDestination = NavTarget.Welcome.route,
//                route = NavTarget.GraphOnboard.route
//            ) {
//
//            }
//
//            navigation(
//                startDestination = NavTarget.Login.route,
//                route = NavTarget.GraphAuthentication.route
//            ) {
//
//            }

            navigation(
                startDestination = NavTarget.Note.route,
                route = NavTarget.GraphNote.route
            ) {
                note()
            }

            navigation(
                startDestination = NavTarget.Folder.route,
                route = NavTarget.GraphFolder.route
            ) {
                folder()
            }

            navigation(
                startDestination = NavTarget.Todo.route,
                route = NavTarget.GraphTodo.route
            ) {
                todo()
            }
        }
    }
}

