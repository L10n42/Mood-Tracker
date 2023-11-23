package com.kappdev.moodtracker.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInLeft(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
        animationSpec = tween(DEFAULT_ANIM_DURATION)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInRight(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
        animationSpec = tween(DEFAULT_ANIM_DURATION)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutRight(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
        animationSpec = tween(DEFAULT_ANIM_DURATION)
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutLeft(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
        animationSpec = tween(DEFAULT_ANIM_DURATION)
    )
}

private const val DEFAULT_ANIM_DURATION = 700