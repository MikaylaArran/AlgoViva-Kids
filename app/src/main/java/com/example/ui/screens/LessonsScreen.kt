package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Lesson
import com.example.data.LessonsData
import com.example.data.UserProgress
import com.example.ui.AppScreen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonsListScreen(
    progress: UserProgress,
    onBack: () -> Unit,
    onStartLesson: (Lesson) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Storybook 📖", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("lessons_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Main Screen"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GalacticBackground,
                    titleContentColor = PurpleSpaceDefault,
                    navigationIconContentColor = PurpleSpaceDefault
                )
            )
        },
        containerColor = GalacticBackground,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome to Science Academy! Choose a magic storybook and learn the secrets of AI with friends!",
                    color = SoftLavender,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )
            }

            items(LessonsData.lessonsList) { lesson ->
                val isCompleted = progress.isLessonCompleted(lesson.id)
                LessonRowItem(
                    lesson = lesson,
                    isCompleted = isCompleted,
                    onPlay = { onStartLesson(lesson) }
                )
            }
        }
    }
}

@Composable
fun LessonRowItem(
    lesson: Lesson,
    isCompleted: Boolean,
    onPlay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("lesson_item_${lesson.id}")
            .border(
                width = 2.dp,
                color = if (isCompleted) TealSparkle else PurpleSpaceDefault.copy(alpha = 0.4f),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CosmicSurface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Character Badge
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(if (isCompleted) TealSparkle.copy(alpha = 0.15f) else PurpleSpaceDefault.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = lesson.iconEmoji, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = lesson.category.uppercase(),
                            color = if (isCompleted) TealSparkle else PurpleSpaceDefault,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        if (isCompleted) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed storybook",
                                tint = TealSparkle,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Text(
                        text = lesson.title,
                        color = OnSurfaceText,
                        fontWeight = FontWeight.Black,
                        fontSize = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = lesson.shortDescription,
                color = SoftLavender,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Reward: ", color = SoftLavender, fontSize = 12.sp)
                    Text(text = "⭐ +${lesson.rewardXp} XP", color = GoldenSun, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = onPlay,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) TealSparkle else PurpleSpaceDefault
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isCompleted) "Read Again 🔁" else "Start Quest 🚀",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// --- LESSON STEP SCREEN (WIZARD DETAILS) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    lesson: Lesson,
    currentStepIndex: Int,
    onBack: () -> Unit,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val step = lesson.steps.getOrNull(currentStepIndex) ?: return
    val totalSteps = lesson.steps.size
    val progressPercent = (currentStepIndex + 1).toFloat() / totalSteps

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lesson.title, fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GalacticBackground,
                    titleContentColor = PurpleSpaceDefault,
                    navigationIconContentColor = PurpleSpaceDefault
                )
            )
        },
        containerColor = GalacticBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Step Progress HUD
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Storybook Progress",
                        color = SoftLavender,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Page ${currentStepIndex + 1} of $totalSteps",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progressPercent },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NeonCyan,
                    trackColor = DarkCardAccent
                )
            }

            // Central Lesson Content Panel
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Character animated bubble avatar
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(PurpleSpaceDefault.copy(alpha = 0.3f), Color.Transparent)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = step.characterEmoji,
                                fontSize = 56.sp
                            )
                        }

                        // Title
                        Text(
                            text = step.title,
                            color = NeonCyan,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        // Fun Kid Explanation
                        Text(
                            text = step.explanation,
                            color = OnSurfaceText,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center
                        )

                        // Magical Analogy Card (Highly critical for children's UI!)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkCardAccent)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "💡", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "FUN ANALOGY!",
                                        color = GoldenSun,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = step.analogy,
                                    color = SoftLavender,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Sparky's Quick Fact Tip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = PurpleSpaceDefault.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⭐", fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                            Text(
                                text = step.visualActionTip,
                                color = SoftLavender,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Bottom Navigation Controller Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = onPrevStep,
                    enabled = currentStepIndex > 0,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (currentStepIndex > 0) CosmicSurface else Color.Transparent)
                        .testTag("step_prev_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Prev page",
                        tint = if (currentStepIndex > 0) OnSurfaceText else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Next or Complete Quest Button
                Button(
                    onClick = onNextStep,
                    modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .testTag("step_next_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStepIndex == totalSteps - 1) TealSparkle else PurpleSpaceDefault
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (currentStepIndex == totalSteps - 1) "Complete Quest! 🎉" else "Turn Page 👉",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
