package com.kappdev.moodtracker.presentation.options.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kappdev.moodtracker.R
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import com.kappdev.moodtracker.presentation.common.components.VerticalSpace
import com.kappdev.moodtracker.presentation.common.components.convexEffect
import com.kappdev.moodtracker.presentation.common.rememberMutableDialogState
import com.kappdev.moodtracker.presentation.options.OptionsViewModel
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch

@Composable
fun OptionsScreen(
    navController: NavHostController,
    settings: SettingsManager,
    viewModel: OptionsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val theme by settings.getValueAsState(Settings.Theme)
    val mainScreen by settings.getValueAsState(Settings.MainScreen)
    val remainder by settings.getValueAsState(Settings.Reminder)

    val themeDialogState = rememberMutableDialogState(initialData = theme)
    val mainScreenDialog = rememberMutableDialogState(initialData = mainScreen)
    val permissionDialog = rememberMutableDialogState(initialData = Unit)

    fun manageSettings(block: suspend SettingsManager.() -> Unit) {
        scope.launch { with(settings) { block() } }
    }

    fun setReminder(enabled: Boolean) {
        val newRemainder = remainder.copy(enabled = enabled)
        manageSettings { setValueTo(Settings.Reminder, newRemainder) }
        viewModel.updateRemainder(newRemainder)
    }

    LaunchedEffect(Unit) {
        viewModel.turnOffReminder.collect {
            settings.setValueTo(Settings.Reminder, remainder.copy(enabled = false))
        }
    }

    if (permissionDialog.isVisible.value) {
        AlarmPermissionDialog(
            onDismiss = permissionDialog::hideDialog,
            onGrant = { context.openExactAlarmSettingPage() }
        )
    }

    MainScreenDialog(
        state = mainScreenDialog,
        onDismiss = mainScreenDialog::hideDialog,
        onConfirm = { newMainScreen ->
            manageSettings { setValueTo(Settings.MainScreen, newMainScreen) }
        }
    )

    ThemeDialog(
        state = themeDialogState,
        onDismiss = themeDialogState::hideDialog,
        onConfirm = { newTheme ->
            manageSettings { setValueTo(Settings.Theme, newTheme) }
        }
    )

    val timeDialog = rememberMaterialDialogState()
    TimePicker(
        initTime = remainder.time,
        state = timeDialog,
        closePicker = timeDialog::hide,
        onTimeSelect = { selectedTime ->
            val newRemainder = remainder.copy(time = selectedTime)
            manageSettings { setValueTo(Settings.Reminder, newRemainder) }
            viewModel.updateRemainder(newRemainder)
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                setReminder(true)
            }
        }
    )

    Scaffold(
        topBar = {
            OptionsTopBar {
                navController.popBackStack()
            }
        }
    ) { padValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padValues)
        ) {
            ItemsGroup {
                SwitchItem(
                    title = stringResource(R.string.reminder_option),
                    icon = Icons.Rounded.Notifications,
                    checked = remainder.enabled,
                    onCheckedChange = { enabled ->
                        when {
                            (enabled && viewModel.needAlarmPermission()) -> permissionDialog.showDialog()
                            (enabled && context.needNotificationPermission()) -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            else -> setReminder(enabled)
                        }
                    }
                )
                Item(
                    title = stringResource(R.string.remainder_time),
                    icon = Icons.Rounded.Schedule,
                    subTitle = remainder.time.toString(),
                    enabled = remainder.enabled,
                    onClick = timeDialog::show
                )
            }

            VerticalSpace(16.dp)

            ItemsGroup {
                Item(
                    title = stringResource(R.string.color_mode_option),
                    icon = Icons.Rounded.Contrast,
                    subTitle = stringResource(theme.titleRes),
                    onClick = {
                        themeDialogState.showDialog(theme)
                    }
                )
                Item(
                    title = stringResource(R.string.main_screen_option),
                    icon = Icons.Rounded.Home,
                    subTitle = stringResource(mainScreen.titleRes),
                    onClick = {
                        mainScreenDialog.showDialog(mainScreen)
                    }
                )
            }

            VerticalSpace(16.dp)

            ItemsGroup {
                Item(
                    title = stringResource(R.string.rate_the_app_option),
                    icon = Icons.Rounded.Star,
                    onClick = {
                        viewModel.rateTheApp.launch()
                    }
                )
            }
        }
    }
}

private fun Context.needNotificationPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        return permissionStatus != PackageManager.PERMISSION_GRANTED
    } else {
        return false
    }
}

private fun Context.openExactAlarmSettingPage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        startActivity(
            Intent(
                ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                Uri.parse("package:$packageName")
            )
        )
    }
}

@Composable
private fun SwitchItem(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    ItemLayout(modifier = modifier) {
        ItemIcon(icon)

        ItemTitle(
            text = title,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun Item(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    subTitle: String? = null,
    onClick: () -> Unit
) {
    ItemLayout(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        ItemIcon(
            icon = icon,
            enabled = enabled
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ItemTitle(
                text = title,
                enabled = enabled
            )

            if (subTitle != null) {
                ItemSubTitle(subTitle)
            }
        }
    }
}

@Composable
private fun ItemsGroup(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.settingView(),
        content = content
    )
}

@Composable
private fun ItemLayout(
    modifier: Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    content: @Composable RowScope.() -> Unit
) {
    val clickModifier = onClick?.let { Modifier.clickable(enabled = enabled, onClick = it) } ?: Modifier
    Row(
        modifier = modifier
            .height(ItemHeight)
            .fillMaxWidth()
            .then(clickModifier)
            .padding(horizontal = 8.dp),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        content = content
    )
}

@Composable
private fun ItemIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Icon(
        imageVector = icon,
        modifier = modifier,
        contentDescription = null,
        tint = when {
            enabled -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onBackground
        }
    )
}

@Composable
private fun ItemTitle(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Text(
        text = text,
        maxLines = 1,
        fontSize = 16.sp,
        modifier = modifier,
        fontWeight = FontWeight.Medium,
        overflow = TextOverflow.Ellipsis,
        color = when {
            enabled -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onBackground
        }
    )
}

@Composable
private fun ItemSubTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        maxLines = 1,
        fontSize = 12.sp,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onBackground
    )
}

private fun Modifier.settingView() = composed {
    this
        .padding(horizontal = 4.dp)
        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
        .convexEffect(RoundedCornerShape(4.dp))
}


private val ItemHeight = 50.dp