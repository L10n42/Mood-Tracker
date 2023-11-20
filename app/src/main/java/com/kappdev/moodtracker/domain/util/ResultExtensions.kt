package com.kappdev.moodtracker.domain.util

fun <T> Result.Companion.fail(message: String): Result<T> {
    return failure(Exception(message))
}

fun <T> Result<T>.messageOrNull(): String? {
    return this.exceptionOrNull()?.message
}
