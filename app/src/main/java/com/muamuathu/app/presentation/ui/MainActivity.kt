package com.muamuathu.app.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.handleNavEvent
import com.muamuathu.app.presentation.graph.*
import com.muamuathu.app.presentation.ui.tab.BottomTabItem
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
        val eventHandler = initEventHandler()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val bottomSheetScreen by remember { derivedStateOf { navBackStackEntry?.destination?.route.orEmpty() } }

        LaunchedEffect(key1 = "Navigation Event Handler") {
            eventHandler.navEvent().collect {
                navController.handleNavEvent(it)
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val tabs = remember { BottomTabItem.values() }
                AwareBottomBar(navController, tabs)
            },
        ) {
            Box(modifier = Modifier.padding(it)) {
                val data = navBackStackEntry?.destination?.route
                NavTarget.Note.route == data || NavTarget.Folder.route == data || NavTarget.Todo.route == data

                NavGraph(navController)
            }

//            var openBottomSheet by rememberSaveable { mutableStateOf(false) }
//            if (TextUtils.equals(
//                    NavTarget.NoteAdd.route,
//                    bottomSheetScreen
//                ) || TextUtils.equals(NavTarget.FolderAdd.route, bottomSheetScreen)
//            ) {
//                ModalBottomSheet(onDismissRequest = { openBottomSheet = false }) {
//                    SheetLayout(currentScreen = bottomSheetScreen)
//                }
//            }
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
            navigation(
                startDestination = NavTarget.Login.route,
                route = NavTarget.GraphAuthentication.route
            ) {
                authentication()
            }

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

//@Composable
//fun SheetLayout(currentScreen: String) {
//    when (currentScreen) {
//        NavTarget.NoteAdd.route -> Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 10.dp)) { ScreenNewNote() }
//        NavTarget.FolderAdd.route -> Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 10.dp)) { ScreenNewFolder() }
//        else -> Column(Modifier.size(1.dp)) {}
//    }
//}

