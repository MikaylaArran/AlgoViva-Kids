package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AiAcademyViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RobotGameScreen(
    viewModel: AiAcademyViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentItem by viewModel.currentTrainingItem.collectAsState()
    val confidence by viewModel.robotGameConfidence.collectAsState()
    val streak by viewModel.robotGameStreak.collectAsState()
    val feedbackMsg by viewModel.robotFeedbackMsg.collectAsState()
    val isRobotHappy by viewModel.isRobotHappy.collectAsState()

    val animatedConfidenceProgress by animateFloatAsState(
        targetValue = confidence / 100f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "confidence"
    )

    // Animated robot mood emoji
    val robotMoodEmoji = when (isRobotHappy) {
        true -> "🥳"
        false -> "🤪"
        null -> "🤖"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Brain Model Trainer 🎯", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("game_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.resetRobotTrainingGame() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset Game")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GalacticBackground,
                    titleContentColor = PurpleSpaceDefault,
                    navigationIconContentColor = PurpleSpaceDefault,
                    actionIconContentColor = PurpleSpaceDefault
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Top HUD: Brain Model Accuracy ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CosmicSurface, RoundedCornerShape(20.dp))
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Model Confidence: ",
                            color = SoftLavender,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Supervised learning accuracy info",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "$confidence%",
                        color = if (confidence >= 80) TealSparkle else if (confidence >= 40) GoldenSun else CoralPink,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Accuracy bar
                LinearProgressIndicator(
                    progress = { animatedConfidenceProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = NeonCyan,
                    trackColor = DarkCardAccent
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Training analytics metrics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Current Streak: $streak",
                        color = GoldenSun,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Target Goal: 100% Accuracy",
                        color = SoftLavender,
                        fontSize = 12.sp
                    )
                }
            }

            // --- Sparky the Animated Kid Robot ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Robot emoji floating with custom pulse
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(NeonCyan, NeonPink, PurpleSpaceDefault, NeonCyan)
                            )
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(CosmicSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = robotMoodEmoji,
                            fontSize = 62.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Voice Speech Bubble
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (isRobotHappy == true) TealSparkle else if (isRobotHappy == false) CoralPink else PurpleSpaceDefault.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 4.dp, bottomStart = 24.dp)
                        ),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 4.dp, bottomStart = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface)
                ) {
                    Text(
                        text = feedbackMsg,
                        color = OnSurfaceText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // --- Central Training Interactive Flashcard ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .shadow(12.dp, RoundedCornerShape(24.dp))
                        .border(width = 2.dp, color = NeonCyan.copy(alpha = 0.6f), shape = RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TRAINING CARD",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Large emoji
                        Text(
                            text = currentItem.emoji,
                            fontSize = 80.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Text(
                            text = currentItem.name.uppercase(),
                            color = OnSurfaceText,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Can you classify me below?",
                            color = SoftLavender,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // --- Classification Pills (Labelling Options) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Select Supervised Label👇",
                    color = SoftLavender,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    viewModel.robotGameCategories.forEach { category ->
                        val pillColor = when (category) {
                            "Food" -> CoralPink
                            "Vehicle" -> NeonCyan
                            "Animal" -> PurpleSpaceDefault
                            else -> PurpleSpaceDefault
                        }

                        val categoryEmoji = when (category) {
                            "Food" -> "🍏 "
                            "Vehicle" -> "🚗 "
                            "Animal" -> "🦁 "
                            else -> ""
                        }

                        Button(
                            onClick = { viewModel.submitTrainingClassification(category) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("classification_button_$category"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = pillColor)
                        ) {
                            Text(
                                text = "$categoryEmoji$category",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
