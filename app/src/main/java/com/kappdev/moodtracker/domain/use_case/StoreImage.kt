package com.kappdev.moodtracker.domain.use_case

import android.content.Context
import android.net.Uri
import com.kappdev.moodtracker.domain.util.fail
import com.kappdev.moodtracker.domain.util.Result
import com.kappdev.moodtracker.domain.util.StoreImageException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class StoreImage @Inject constructor(
    private val context: Context
) {

    /**
     * Copy image from the Uri to app images folder
     * @return Path of the image
     * */
    operator fun invoke(uri: Uri): Result<String> {
        val imageFileName = "IMG_${System.currentTimeMillis()}.jpg"

        val destinationDirectory = File(context.filesDir, "images")
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs()
        }

        val destinationFile = File(destinationDirectory, imageFileName)
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null

        return try {
            outputStream = FileOutputStream(destinationFile)
            inputStream = context.contentResolver.openInputStream(uri)

            if (inputStream != null) {
                copyFile(inputStream, outputStream)
                Result.Success(destinationFile.absolutePath)
            } else {
                Result.Failure(StoreImageException("Error"))
            }
        } catch (e: Exception) {
            Result.Failure(StoreImageException("Error"))
        } catch (e: FileNotFoundException) {
            Result.Failure(StoreImageException("Image not found"))
        } finally {
            outputStream?.close()
            inputStream?.close()
        }
    }

    private fun copyFile(input: InputStream, output: FileOutputStream) {
        val buffer = ByteArray(4 * 1024)
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
            output.write(buffer, 0, read)
        }
        output.flush()
    }
}