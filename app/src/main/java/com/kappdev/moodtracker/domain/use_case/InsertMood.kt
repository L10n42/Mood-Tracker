package com.kappdev.moodtracker.domain.use_case

import com.kappdev.moodtracker.domain.model.Image
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.repository.MoodRepository
import com.kappdev.moodtracker.domain.util.Result
import com.kappdev.moodtracker.domain.util.ResultState
import com.kappdev.moodtracker.domain.util.emitFailure
import com.kappdev.moodtracker.domain.util.emitLoading
import com.kappdev.moodtracker.domain.util.emitSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class InsertMood @Inject constructor(
    private val repository: MoodRepository,
    private val storeImage: StoreImage
) {

    suspend operator fun invoke(date: LocalDate?, moodType: MoodType?, note: String, images: List<Image>): Flow<ResultState<Long>> = flow {
        ifValidData(date, moodType) { date, type ->
            emitLoading("Storing images")
            val storedImages = storeImages(images)

            emitLoading("Saving mood")
            val mood = Mood(date, type, note, storedImages)
            val result = repository.insertMood(mood)
            emitResult(result)
        }
    }

    private suspend fun FlowCollector<ResultState<Long>>.ifValidData(
        date: LocalDate?, moodType: MoodType?,
        block: suspend FlowCollector<ResultState<Long>>.(date: LocalDate, moodType: MoodType) -> Unit
    ) {
        when {
            (date == null) -> emitFailure(Exception("Incorrect date"))
            (moodType == null) -> emitFailure(Exception("Choose a mood"))
            else -> block(date, moodType)
        }
    }

    private suspend fun FlowCollector<ResultState<Long>>.emitResult(resultId: Long) {
        if (resultId > 0) {
            emitSuccess(resultId)
        } else {
            emitFailure(Exception("Insert error"))
        }
    }

    private suspend fun FlowCollector<ResultState<Long>>.storeImages(images: List<Image>): List<String>? {
        return images.mapNotNull { image ->
            when (image) {
                is Image.Stored -> image.path
                is Image.NotStored -> storeImage(image)
            }
        }.ifEmpty { null }
    }

    private suspend fun FlowCollector<ResultState<Long>>.storeImage(image: Image.NotStored): String? {
        return when (val storeResult = storeImage(image.uri)) {
            is Result.Success -> storeResult.value
            is Result.Failure -> {
                emitFailure(storeResult.exception)
                null
            }
        }
    }
}