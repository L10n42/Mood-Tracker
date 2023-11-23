package com.kappdev.moodtracker.presentation.options.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Source
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kappdev.moodtracker.domain.repository.SettingsManager
import com.kappdev.moodtracker.domain.util.Settings
import com.kappdev.moodtracker.domain.util.Theme
import com.kappdev.moodtracker.presentation.common.components.VerticalSpace
import com.kappdev.moodtracker.presentation.common.components.convexEffect
import com.kappdev.moodtracker.presentation.common.rememberMutableDialogState
import kotlinx.coroutines.launch

@Composable
fun OptionsScreen(
    navController: NavHostController,
    settings: SettingsManager
) {
    val scope = rememberCoroutineScope()

    val themeName by settings.getValueAsState(Settings.Theme)
    val theme = Theme.valueOf(themeName)
    val themeDialogState = rememberMutableDialogState(initialData = theme)

    fun manageSettings(block: suspend SettingsManager.() -> Unit) {
        scope.launch { with(settings) { block() } }
    }

    ThemeDialog(
        state = themeDialogState,
        onDismiss = themeDialogState::hideDialog,
        onConfirm = { newTheme ->
            manageSettings { setValueTo(Settings.Theme, newTheme.name) }
            themeDialogState.hideDialog()
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
            Item(
                title = "Remainder",
                icon = Icons.Rounded.Notifications,
                subTitle = "21:00",
                modifier = Modifier.settingView(),
                onClick = { /* TODO */ }
            )

            VerticalSpace(16.dp)

            Column(
                modifier = Modifier.settingView()
            ) {
                Item(
                    title = "Color mode",
                    icon = Icons.Rounded.Contrast,
                    subTitle = stringResource(theme.titleRes),
                    onClick = {
                        themeDialogState.showDialog(theme)
                    }
                )
                Item(
                    title = "Main screen",
                    icon = Icons.Rounded.Home,
                    subTitle = "Calendar",
                    onClick = { /* TODO */ }
                )
                Item(
                    title = "Something else",
                    icon = Icons.Rounded.Source,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
private fun Item(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(ItemHeight)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                maxLines = 1,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            subTitle?.let {
                Text(
                    text = subTitle,
                    maxLines = 1,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

private fun Modifier.settingView() = composed {
    this
        .padding(horizontal = 4.dp)
        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(4.dp))
        .convexEffect(RoundedCornerShape(4.dp))
}


private val ItemHeight = 50.dp