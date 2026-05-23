package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.UserProgressRepository
import com.example.ui.AiAcademyViewModel
import com.example.ui.AiAcademyViewModelFactory
import com.example.ui.AppScreen
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Edge-to-edge support for beautiful full bleed layout
        enableEdgeToEdge()

        // Room database & repository setup
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = UserProgressRepository(database.userProgressDao())
        
        // ViewModel instantiation via factory
        val factory = AiAcademyViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[AiAcademyViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: AiAcademyViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val progress by viewModel.userProgress.collectAsState()

    // Handle back button gesture cleanly
    if (currentScreen != AppScreen.DASHBOARD) {
        BackHandler {
            when (currentScreen) {
                AppScreen.LESSON_DETAIL -> viewModel.navigateTo(AppScreen.LESSON_LIST)
                else -> viewModel.navigateTo(AppScreen.DASHBOARD)
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        
        when (currentScreen) {
            AppScreen.DASHBOARD -> {
                DashboardScreen(
                    progress = progress,
                    onNavigate = { viewModel.navigateTo(it) },
                    modifier = screenModifier
                )
            }
            AppScreen.LESSON_LIST -> {
                LessonsListScreen(
                    progress = progress,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    onStartLesson = { viewModel.startLesson(it) },
                    modifier = screenModifier
                )
            }
            AppScreen.LESSON_DETAIL -> {
                val lesson = viewModel.activeLesson.collectAsState().value
                val stepIdx = viewModel.activeLessonStepIndex.collectAsState().value
                if (lesson != null) {
                    LessonDetailScreen(
                        lesson = lesson,
                        currentStepIndex = stepIdx,
                        onBack = { viewModel.navigateTo(AppScreen.LESSON_LIST) },
                        onNextStep = { viewModel.nextLessonStep() },
                        onPrevStep = { viewModel.prevLessonStep() },
                        modifier = screenModifier
                    )
                }
            }
            AppScreen.ROBOT_TRAIN_GAME -> {
                RobotGameScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.GENAI_SANDBOX -> {
                SandboxScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
            AppScreen.ROBO_TUTOR -> {
                RoboTutorScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                    modifier = screenModifier
                )
            }
        }
    }
}
