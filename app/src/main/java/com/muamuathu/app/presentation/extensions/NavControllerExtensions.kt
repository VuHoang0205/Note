package com.muamuathu.app.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.graph.NavTarget
import java.net.URLEncoder

fun NavController.navigateToWeb(url: String) {
    val encodedUrl = URLEncoder.encode(url, Charsets.UTF_8.name())
    navigate(
        NavTarget.WebView.route.replace(
            "{${NavTarget.WEB_URL_KEY}}",
            encodedUrl
        )
    )
}

fun NavController.navigate(route: String, keyValue: Pair<String, String>) {
    val encodedParam = URLEncoder.encode(keyValue.second, Charsets.UTF_8.name())
    val newRoute = route.replace("{${keyValue.first}}", encodedParam)
    navigate(newRoute)
}

fun NavHostController.handleNavEvent(navEvent: NavEvent) {
    when (navEvent) {
        is NavEvent.None -> {}
        is NavEvent.Action -> {
            navigate(navEvent.target.route)
        }
        is NavEvent.ActionWithValue -> {
            navigate(navEvent.target.route, navEvent.value)
        }
        is NavEvent.ActionInclusive -> {
            navigate(navEvent.target.route) {
                popUpTo(navEvent.inclusiveTarget.route) {
                    inclusive = true
                }
            }
        }
        is NavEvent.PopBackStackWithTarget -> {
            popBackStack(navEvent.target.route, inclusive = navEvent.inclusive)
        }
        is NavEvent.NavigateUp -> {
            navigateUp()
        }

        is NavEvent.ActionWeb -> {
            navigateToWeb(navEvent.url)
        }
        is NavEvent.PopBackStack -> {
            popBackStack()
        }

        is NavEvent.ActionWithPopUp -> {
            navigate(navEvent.target.route) {
                popUpTo(navEvent.popupTarget.route) {
                    inclusive = navEvent.inclusive
                }
            }
        }
    }
}

@Composable
fun NavBackStackEntry.getParentEntry(navController: NavHostController, parentEntry: String): NavBackStackEntry? {
    val navBackStackEntry = remember(this) {
        try {
            navController.getBackStackEntry(parentEntry)
        } catch (e: Exception) {
            null
        }
    }
    return navBackStackEntry
}