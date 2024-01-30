package com.kappdev.moodtracker.domain.use_case

import android.app.Application
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.repository.MoodRepository
import com.kappdev.moodtracker.domain.util.ResultState
import com.kappdev.moodtracker.domain.util.emitFailure
import com.kappdev.moodtracker.domain.util.emitLoading
import com.kappdev.moodtracker.domain.util.emitSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class DeleteMood @Inject constructor(
    private val repository: MoodRepository,
    private val app: Application
) {

    operator fun invoke(mood: Mood?): Flow<ResultState<Unit>> = flow {
        if (mood != null) {
            deleteImages(mood)
            deleteData(mood)
        } else {
            emitFailure(Exception(app.getString(R.string.no_mood_data)))
        }
    }

    private suspend fun FlowCollector<ResultState<Unit>>.deleteData(mood: Mood) {
        emitLoading(app.getString(R.string.deleting_data))

        val deleted = repository.deleteByDate(mood.date)
        if (deleted > 0) {
            emitSuccess(Unit)
        } else {
            emitFailure(Exception(app.getString(R.string.delete_mood_error)))
        }
    }

    private suspend fun FlowCollector<ResultState<Unit>>.deleteImages(mood: Mood) {
        if (!mood.images.isNullOrEmpty()) {
            emitLoading(app.getString(R.string.deleting_images))
            mood.images.forEach(::deleteImage)
        }
    }

    private fun deleteImage(filePath: String) {
        File(filePath).deleteOnExit()
    }
}