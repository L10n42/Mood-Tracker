package com.kappdev.moodtracker.presentation.mood_screen.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.presentation.common.components.CustomAlertDialog
import com.kappdev.moodtracker.presentation.common.components.DividedContent
import com.kappdev.moodtracker.presentation.common.components.VerticalSpace
import com.kappdev.moodtracker.presentation.common.rememberMutableDialogState
import com.kappdev.moodtracker.presentation.mood_screen.MoodScreenViewModel
import java.time.LocalDate

@Composable
fun MoodScreen(
    navController: NavHostController,
    date: LocalDate?,
    viewModel: MoodScreenViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val saveDialogState = rememberMutableDialogState(null)

    LaunchedEffect(date) {
        viewModel.changeDate(date)
        viewModel.getMoodData {
            navController.popBackStack()
        }
    }

    if (saveDialogState.isVisible.value) {
        CustomAlertDialog(
            title = stringResource(R.string.unsaved_changes_title),
            text = stringResource(R.string.unsaved_changes_msg),
            confirmText = stringResource(R.string.btn_save),
            cancelText = stringResource(R.string.btn_discard),
            onDismiss = saveDialogState::hideDialog,
            onCancel = {
                saveDialogState.hideDialog()
                navController.popBackStack()
            },
            onConfirm = {
                saveDialogState.hideDialog()
                viewModel.saveMood { navController.popBackStack() }
            }
        )
    }

    Scaffold(
        topBar = {
            DividedContent(
                isDividerVisible = scrollState.canScrollBackward
            ) {
                MoodTopBar(
                    date = viewModel.date,
                    onBack = {
                        when {
                            viewModel.hasUnsavedChanges() -> saveDialogState.showDialog()
                            else -> navController.popBackStack()
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            DoneButton {
                viewModel.saveMood { navController.popBackStack() }
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
                onTakePhoto = { /*TODO*/ },
                onFromGallery = { /*TODO*/ }
            )
        }
    }
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