package com.kappdev.moodtracker.presentation.mood_chart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.use_case.CopyQuote
import com.kappdev.moodtracker.domain.use_case.GetMonthChartData
import com.kappdev.moodtracker.domain.use_case.GetWeekChartData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoodChartScreenViewModel @Inject constructor(
    private val getMonthChartData: GetMonthChartData,
    private val getWeekChartData: GetWeekChartData,
    val copyQuote: CopyQuote
) : ViewModel() {

    var chartFrame by mutableStateOf(ChartFrame.WEEK)
        private set

    var chartType by mutableStateOf(ChartType.LINE)
        private set

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

    fun changeChartType(newType: ChartType) {
        chartType = newType
        updateChartData()
    }

    fun changeChartFrame(newFrame: ChartFrame) {
        chartFrame = newFrame
        resetDate()
        updateChartData()
    }

    private fun updateChartData() {
        chartDataJob?.cancel()
        chartDataJob = when (chartFrame) {
            ChartFrame.MONTH -> {
                getMonthChartData(currentDate).onEach(::updateData).launchIn(viewModelScope)
            }
            ChartFrame.WEEK -> {
                getWeekChartData(currentDate).onEach(::updateData).launchIn(viewModelScope)
            }
        }
    }

    private fun updateData(newData: Map<Int, MoodType?>) {
        this.data = newData
    }

    private fun resetDate() {
        currentDate = LocalDate.now()
    }
}