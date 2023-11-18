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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@HiltViewModel
class MoodScreenViewModel @Inject constructor(
    private val insertMood: InsertMood,
    private val getMoodByDate: GetMoodByDate
) : ViewModel() {

    var date by mutableStateOf<LocalDate?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedMood by mutableStateOf<MoodType>(MoodType.Good)
        private set

    var note by mutableStateOf("")
        private set

    fun saveMood(onSuccess: () -> Unit) {
        viewModelScope.launchLoading(Dispatchers.IO) {
            val result = insertMood(packMood())
            if (result.isSuccess) {
                withContext(Dispatchers.Main) { onSuccess() }
            }
        }
    }

    fun getMoodData(onFailure: () -> Unit) {
        viewModelScope.launchLoading(Dispatchers.IO) {
            val mood = date?.let { getMoodByDate(it) }
            if (date != null && mood != null) {
                unpackMood(mood)
            } else if (date == null) {
                withContext(Dispatchers.Main) { onFailure() }
            }
        }
    }

    private fun unpackMood(mood: Mood) {
        note = mood.note
        selectedMood = mood.type
    }

    private fun packMood(): Mood {
        return Mood(
            note = note,
            type = selectedMood,
            date = date!!
        )
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
    ) {
        viewModelScope.launch(context) {
            this@MoodScreenViewModel.isLoading = true
            block()
            this@MoodScreenViewModel.isLoading = false
        }
    }
}