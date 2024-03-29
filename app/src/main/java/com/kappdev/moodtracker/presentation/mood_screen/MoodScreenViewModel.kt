package com.kappdev.moodtracker.presentation.mood_screen

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.model.Image
import com.kappdev.moodtracker.domain.model.Mood
import com.kappdev.moodtracker.domain.model.MoodType
import com.kappdev.moodtracker.domain.use_case.CopyNote
import com.kappdev.moodtracker.domain.use_case.DeleteMood
import com.kappdev.moodtracker.domain.use_case.GetMoodByDate
import com.kappdev.moodtracker.domain.use_case.InsertMood
import com.kappdev.moodtracker.domain.use_case.ShareImage
import com.kappdev.moodtracker.domain.util.ResultState
import com.kappdev.moodtracker.domain.util.StoreImageException
import com.kappdev.moodtracker.domain.util.Toaster
import com.kappdev.moodtracker.presentation.common.DialogState
import com.kappdev.moodtracker.presentation.common.mutableDialogStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoodScreenViewModel @Inject constructor(
    private val insertMood: InsertMood,
    private val deleteMood: DeleteMood,
    private val shareImage: ShareImage,
    private val getMoodByDate: GetMoodByDate,
    private val copyNote: CopyNote,
    private val toaster: Toaster
) : ViewModel() {
    private var originalMood: Mood? = null

    var date by mutableStateOf<LocalDate?>(null)
        private set

    private var _loadingDialogState = mutableDialogStateOf<String?>(null)
    val loadingDialogState: DialogState<String?> = _loadingDialogState

    var selectedMood by mutableStateOf<MoodType?>(null)
        private set

    var note by mutableStateOf("")
        private set

    private var _images = mutableStateListOf<Image>()
    val images: List<Image> = _images

    private var saveJob: Job? = null

    fun saveMood(onSuccess: () -> Unit) {
        saveJob?.cancel()
        saveJob = viewModelScope.launch(Dispatchers.IO) {
            insertMood(date, selectedMood, note, images).collect { resultState ->
                when (resultState) {
                    is ResultState.Loading -> _loadingDialogState.showDialog(resultState.message)
                    is ResultState.Failure -> withContext(Dispatchers.Main) {
                        if (resultState.exception !is StoreImageException) {
                            _loadingDialogState.hideDialog()
                        }
                        resultState.exception.message?.let(toaster::show)
                    }
                    is ResultState.Success -> withContext(Dispatchers.Main) {
                        _loadingDialogState.hideDialog()
                        onSuccess()
                    }
                }
            }
        }
    }

    fun deleteMood(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMood(originalMood).collect { resultState ->
                when (resultState) {
                    is ResultState.Loading -> _loadingDialogState.showDialog(resultState.message)
                    is ResultState.Failure -> withContext(Dispatchers.Main) {
                        _loadingDialogState.hideDialog()
                        resultState.exception.message?.let(toaster::show)
                    }
                    is ResultState.Success -> withContext(Dispatchers.Main) {
                        _loadingDialogState.hideDialog()
                        onSuccess()
                    }
                }
            }
        }
    }

    fun getMoodData(onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingDialogState.showDialog(null)
            val mood = date?.let { getMoodByDate(it) }
            when {
                (date != null && mood != null) -> unpackMood(mood)
                (date == null) -> withContext(Dispatchers.Main) { onFailure() }
            }
            _loadingDialogState.hideDialog()
        }
    }

    private fun unpackMood(mood: Mood) {
        note = mood.note
        selectedMood = mood.type
        mood.images?.map(Image::Stored)?.let(::updateImages)
        originalMood = mood
    }

    fun hasUnsavedChanges(): Boolean {
        return if (originalMood == null) {
            note.isNotEmpty() || images.isNotEmpty() || selectedMood != null
        } else {
            note != originalMood?.note || selectedMood != originalMood?.type || unsavedImages()
        }
    }

    private fun unsavedImages(): Boolean {
        val countChanged = (originalMood?.images != null && images.size != originalMood?.images?.size)
        val hasNotStoredImages = images.count { it is Image.NotStored } > 0
        return (countChanged || hasNotStoredImages)
    }

    fun shareImage(image: Image) {
        this.shareImage(image.model)
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

    fun addImage(uri: Uri) {
        _images.add(Image.NotStored(uri))
    }

    fun addImages(images: List<Uri>) {
        _images.addAll(images.map { Image.NotStored(it) })
    }

    private fun updateImages(images: List<Image.Stored>) {
        _images.removeIf { it is Image.Stored }
        _images.addAll(images)
    }

    fun removeImage(image: Image) {
        if (image is Image.Stored) {
            File(image.path).deleteOnExit()
        }
        _images.remove(image)
    }

    fun copyNote() = copyNote(note)

    fun getOriginalDate() = originalMood?.date

    fun canDeleteMood(): Boolean {
        return if (originalMood != null) {
            true
        } else {
            toaster.show(R.string.nothing_to_delete)
            false
        }
    }
}