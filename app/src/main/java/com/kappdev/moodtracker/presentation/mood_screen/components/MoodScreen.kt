package com.kappdev.moodtracker.presentation.mood_screen.components

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.presentation.common.components.CustomAlertDialog
import com.kappdev.moodtracker.presentation.common.components.DividedContent
import com.kappdev.moodtracker.presentation.common.components.LoadingDialog
import com.kappdev.moodtracker.presentation.common.components.VerticalSpace
import com.kappdev.moodtracker.presentation.common.rememberMutableDialogState
import com.kappdev.moodtracker.presentation.mood_screen.MoodOption
import com.kappdev.moodtracker.presentation.mood_screen.MoodScreenViewModel
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MoodScreen(
    date: LocalDate?,
    viewModel: MoodScreenViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val saveDialogState = rememberMutableDialogState(null)
    val deleteDialogState = rememberMutableDialogState<LocalDate?>(null)

    LaunchedEffect(date) {
        viewModel.changeDate(date)
        viewModel.getMoodData(onFailure = onBack)
    }

    LoadingDialog(viewModel.loadingDialogState)

    if (deleteDialogState.isVisible.value) {
        deleteDialogState.dialogData.value?.let { dialogDate ->
            DeleteDialog(
                date = dialogDate,
                onDismiss = deleteDialogState::hideDialog,
                onConfirm = {
                    viewModel.deleteMood(onBack)
                }
            )
        }
    }

    if (saveDialogState.isVisible.value) {
        UnsavedChangesDialog(
            onDismiss = saveDialogState::hideDialog,
            onCancel = onBack,
            onConfirm = {
                viewModel.saveMood(onSuccess = onBack)
            }
        )
    }

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSaved ->
            if (isSaved) {
                viewModel.addImage(uri)
            }
        }
    )

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            if (uris.isNotEmpty()) {
                viewModel.addImages(uris)
            }
        }
    )

    Scaffold(
        topBar = {
            DividedContent(
                isDividerVisible = scrollState.canScrollBackward
            ) {
                MoodTopBar(
                    date = viewModel.date,
                    onOptionClick = { hidePopup, clickedOption ->
                        when (clickedOption) {
                            MoodOption.CopyNote -> viewModel.copyNote()
                            MoodOption.Delete -> {
                                if (viewModel.canDeleteMood()) {
                                    deleteDialogState.showDialog(viewModel.getOriginalDate())
                                }
                            }
                        }
                        hidePopup()
                    },
                    onBack = {
                        when {
                            viewModel.hasUnsavedChanges() -> saveDialogState.showDialog()
                            else -> onBack()
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            DoneButton {
                viewModel.saveMood(onSuccess = onBack)
            }
        }
    ) { padValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padValues)
                .verticalScroll(scrollState)
        ) {
            val titleModifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

            VerticalSpace(16.dp)

            Title(
                title = stringResource(R.string.mood_picker_title),
                modifier = titleModifier
            )
            MoodPicker(
                selected = viewModel.selectedMood,
                onSelect = viewModel::selectMood
            )

            VerticalSpace(32.dp)

            Title(
                title = stringResource(R.string.quick_note_title),
                modifier = titleModifier
            )
            NoteField(
                note = viewModel.note,
                modifier = contentModifier,
                onNoteChange = viewModel::updateNote
            )

            VerticalSpace(32.dp)

            Title(
                title = stringResource(R.string.photo_title),
                modifier = titleModifier
            )
            PhotoButtons(
                modifier = contentModifier,
                onTakePhoto = {
                    takePictureLauncher.launch(uri)
                },
                onFromGallery = {
                    pickImageLauncher.launch("image/*")
                }
            )

            VerticalSpace(32.dp)

            MoodImages(
                images = viewModel.images,
                modifier = contentModifier,
                removeImage = viewModel::removeImage,
                shareImage = viewModel::shareImage
            )

            VerticalSpace(64.dp)
        }
    }
}

fun Context.createImageFile(): File {
    val imageFileName = "IMG_${System.currentTimeMillis()}"
    return File.createTempFile(imageFileName, ".jpg", externalCacheDir)
}

@Composable
private fun DeleteDialog(
    date: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("MMMM dd, yyyy") }

    CustomAlertDialog(
        title = stringResource(R.string.delete),
        text = stringResource(R.string.delete_warning_msg, date.format(formatter)),
        confirmText = stringResource(R.string.btn_delete),
        onDismiss = onDismiss,
        onConfirm = onConfirm
    )
}

@Composable
private fun UnsavedChangesDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    CustomAlertDialog(
        title = stringResource(R.string.unsaved_changes_title),
        text = stringResource(R.string.unsaved_changes_msg),
        confirmText = stringResource(R.string.btn_save),
        cancelText = stringResource(R.string.btn_discard),
        onDismiss = onDismiss,
        onCancel = onCancel,
        onConfirm = onConfirm
    )
}

@Composable
private fun Title(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        maxLines = 1,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}