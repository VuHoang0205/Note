package com.muamuathu.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.composableWithArgs(
    route: String,
    vararg arguments: String,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments.map { navArgument(it) { defaultValue = "" } }
    ) { backStack ->
        content(backStack)
    }
}

fun NavBackStackEntry.getString(key: String) = arguments?.getString(key)
fun NavBackStackEntry.getInt(key: String) = arguments?.getString(key)?.toIntOrNull() ?: 0
fun NavBackStackEntry.getLong(key: String) = arguments?.getString(key)?.toLongOrNull() ?: 0