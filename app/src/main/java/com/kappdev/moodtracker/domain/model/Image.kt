package com.kappdev.moodtracker.domain.model

import android.net.Uri

sealed class Image(val model: String?) {
    data class Stored(val path: String): Image(path)
    data class NotStored(val uri: Uri): Image(uri.toString())
}
