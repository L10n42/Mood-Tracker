package com.kappdev.moodtracker.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController

fun AnimatedContentTransitionScope<NavBackStackEntry>.getInitialRoute(): String? {
    return this.initialState.destination.route
}

fun <T> NavBackStackEntry.catchValue(key: String): T? {
    return this.savedStateHandle.get<T>(key)
}

fun <T> NavHostController.navigateWithValue(route: String, valueKey: String, value: T?) {
    this.navigate(route)
    this.currentBackStackEntry?.savedStateHandle?.set(valueKey, value)
}

fun <T> NavHostController.goBackWithValue(key: String, value: T?) {
    this.popBackStack()
    this.currentBackStackEntry?.savedStateHandle?.set(key, value)
}