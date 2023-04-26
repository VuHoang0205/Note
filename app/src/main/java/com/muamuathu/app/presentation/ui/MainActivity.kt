package com.muamuathu.app.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.muamuathu.app.presentation.components.AppDialog
import com.muamuathu.app.presentation.components.bottomsheet.AppBottomSheet
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.handleNavEvent
import com.muamuathu.app.presentation.graph.*
import com.muamuathu.app.presentation.ui.tab.BottomTabItem
import com.muamuathu.feature.consent.ConsentInfo
import com.muamuathu.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
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
        val eventHandler = initEventHandler()
        LaunchedEffect(key1 = "Navigation Event Handler") {
            eventHandler.navEvent().collect {
                navController.handleNavEvent(it)
            }
        }
        val sheetState = rememberModalBottomSheetState()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val tabs = remember { BottomTabItem.values() }
                AwareBottomBar(navController, tabs)
            },
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                NavGraph(navController, Modifier.fillMaxSize())
                AppDialog(eventHandler = eventHandler)
                if (eventHandler.bottomSheetEvent().isShowing()) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        containerColor = colorResource(id = R.color.alice_blue),
                        onDismissRequest = {
                            eventHandler.postBottomSheetEvent(BottomSheetEvent.Hide { true })
                        },
                    ) {
                        AppBottomSheet(eventHandler)
                    }
                }
            }
        }
    }

    @Composable
    fun AwareBottomBar(navController: NavController, tabs: Array<BottomTabItem>) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val routes = remember { tabs.map { it.route } }
        if (currentRoute in routes) {
            NavigationBar(containerColor = colorResource(id = R.color.hawkes_blue)) {
                tabs.forEach { tab ->
                    NavigationBarItem(icon = {
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
                        alwaysShowLabel = false,
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
                        })
                }
            }
        }
    }

    @Composable
    fun NavGraph(navController: NavHostController, modifier: Modifier) {
        NavHost(navController, NavTarget.GraphNote.route, modifier) {

            navigation(startDestination = NavTarget.Login.route, route = NavTarget.GraphAuthentication.route) {
                authentication(navController)
            }

            navigation(startDestination = NavTarget.Note.route, route = NavTarget.GraphNote.route) {
                note(navController = navController, route = NavTarget.GraphNote.route)
            }

            navigation(startDestination = NavTarget.Folder.route, route = NavTarget.GraphFolder.route) {
                folder(navController = navController, route = NavTarget.GraphFolder.route)
            }

            navigation(startDestination = NavTarget.Todo.route, route = NavTarget.GraphTodo.route) {
                todo(navController)
            }
        }
    }
}


