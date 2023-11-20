package com.kappdev.moodtracker.presentation.mood_chart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.use_case.GetMonthChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoodChartScreenViewModel @Inject constructor(
    private val getMonthChartData: GetMonthChartData
) : ViewModel() {

    var currentDate by mutableStateOf<LocalDate>(LocalDate.now())
        private set

    var data by mutableStateOf<Map<Int, MoodType?>>(emptyMap())
        private set

    private var chartDataJob: Job? = null

    fun launch() {
        updateChartData()
    }

    fun changeCurrentDate(date: LocalDate) {
        currentDate = date
        updateChartData()
    }

    private fun updateChartData() {
        chartDataJob?.cancel()
        chartDataJob = viewModelScope.launch(Dispatchers.IO) {
            data = getMonthChartData(currentDate)
        }
    }

}