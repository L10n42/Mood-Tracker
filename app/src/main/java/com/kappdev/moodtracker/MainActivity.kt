package com.kappdev.moodtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.kappdev.moodtracker.presentation.navigation.SetupNavGraph
import com.kappdev.moodtracker.ui.theme.MoodTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodTrackerTheme {
                val navController = rememberNavController()

                SetupNavGraph(navController)
            }
        }
    }
}
