package com.kappdev.moodtracker.domain.use_case

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.kappdev.moodtracker.R
import java.io.File
import javax.inject.Inject

class ShareImage @Inject constructor(
    private val context: Context
) {
    private val shareTitle = context.getString(R.string.share_image_title)

    operator fun invoke(path: String) {
        val imageFile = File(path)
        val imageUri = FileProvider.getUriForFile(
            /* context = */ context,
            /* authority = */ "${context.packageName}.provider",
            /* file = */ imageFile
        )

        val sharingIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpg"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, imageUri)
        }

        val chooserIntent = Intent.createChooser(sharingIntent, shareTitle)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }
}