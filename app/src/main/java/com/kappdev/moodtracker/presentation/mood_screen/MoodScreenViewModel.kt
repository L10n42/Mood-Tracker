package com.kappdev.moodtracker.presentation.mood_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.use_case.GetMoodByDate
import com.kappdev.moodtracker.domain.use_case.InsertMood
import com.kappdev.moodtracker.domain.util.Toaster
import com.kappdev.moodtracker.domain.util.messageOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@HiltViewModel
class MoodScreenViewModel @Inject constructor(
    private val insertMood: InsertMood,
    private val getMoodByDate: GetMoodByDate,
    private val toaster: Toaster
) : ViewModel() {

    var date by mutableStateOf<LocalDate?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedMood by mutableStateOf<MoodType?>(null)
        private set

    var note by mutableStateOf("")
        private set

    fun saveMood(onSuccess: () -> Unit) {
        viewModelScope.launchLoading(Dispatchers.IO) {
            val result = insertMood(date, selectedMood, note)

            withContext(Dispatchers.Main) {
                when {
                    result.isSuccess -> onSuccess()
                    result.isFailure -> result.messageOrNull()?.let(toaster::show)
                }
            }
        }
    }

    fun getMoodData(onFailure: () -> Unit) {
        viewModelScope.launchLoading(Dispatchers.IO) {
            val mood = date?.let { getMoodByDate(it) }
            when {
                (date != null && mood != null) -> unpackMood(mood)
                (date == null) -> withContext(Dispatchers.Main) { onFailure() }
            }
        }
    }

    private fun unpackMood(mood: Mood) {
        note = mood.note
        selectedMood = mood.type
    }

    fun selectMood(type: MoodType) {
        this.selectedMood = type
    }

    fun updateNote(newNote: String) {
        this.note = newNote
    }

    fun changeDate(date: LocalDate?) {
        this.date = date
    }

    private fun CoroutineScope.launchLoading(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return this.launch(context) {
            this@MoodScreenViewModel.isLoading = true
            block()
            this@MoodScreenViewModel.isLoading = false
        }
    }
}