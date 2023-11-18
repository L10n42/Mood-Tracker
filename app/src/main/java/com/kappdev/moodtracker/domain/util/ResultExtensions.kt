package com.kappdev.moodtracker.domain.util

fun <T> Result.Companion.fail(message: String): Result<T> {
    return failure(Exception(message))
}
