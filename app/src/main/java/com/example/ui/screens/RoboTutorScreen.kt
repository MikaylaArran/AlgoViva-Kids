package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.ui.AiAcademyViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RoboTutorScreen(
    viewModel: AiAcademyViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val question by viewModel.tutorQuestion.collectAsState()
    val answer by viewModel.tutorAnswer.collectAsState()
    val isLoading by viewModel.isTutorLoading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ask Robo-Tutor 💬", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("tutor_back_button")) {
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Helper Header Robot ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(GoldenSun.copy(alpha = 0.25f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🧠", fontSize = 54.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Sparky's AI Q&A Panel",
                        color = GoldenSun,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Ask me absolutely anything about AI! How do machines count? Do they sleep? Go ahead, ask sparky! 👇",
                        color = SoftLavender,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp)
                    )
                }
            }

            // --- Suggested Kid Questions Pills ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = GoldenSun, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SUGGESTED CURIOSITY TOPICS:",
                            color = GoldenSun,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        viewModel.suggestedQuestions.forEach { item ->
                            val isSelected = question == item
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) GoldenSun else DarkCardAccent)
                                    .clickable { viewModel.setTutorQuestion(item) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = item,
                                    color = if (isSelected) Color.White else OnSurfaceText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // --- Question Input & Ask Button ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = question,
                    onValueChange = { viewModel.setTutorQuestion(it) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tutor_question_input"),
                    placeholder = { Text("Ask something here...", color = Color(0xFF64748B), fontSize = 13.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldenSun,
                        unfocusedBorderColor = DarkCardAccent,
                        focusedContainerColor = CosmicSurface,
                        unfocusedContainerColor = CosmicSurface,
                        focusedTextColor = OnSurfaceText,
                        unfocusedTextColor = OnSurfaceText
                    ),
                    shape = RoundedCornerShape(16.dp),
                    maxLines = 2
                )

                Button(
                    onClick = { viewModel.askRobotTutor() },
                    enabled = !isLoading && question.trim().isNotEmpty(),
                    modifier = Modifier
                        .size(56.dp)
                        .testTag("ask_tutor_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldenSun, disabledContainerColor = DarkCardAccent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Ask",
                        tint = if (question.isNotEmpty()) Color.Black else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // --- ANSWER SCREEN STATE ---
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = GoldenSun)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Sparky is computing mathematical nodes...\nThinking like a machine... 🤖⚡",
                            color = SoftLavender,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                answer != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth().animateContentSize()
                    ) {
                        Text(
                            text = "Sparky says: 🤖👇",
                            color = GoldenSun,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 2.dp, color = GoldenSun.copy(alpha = 0.5f), shape = RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = CosmicSurface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = answer!!,
                                    color = OnSurfaceText,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "💡 Sparky rewarded you +15 XP for asking an educational question!",
                            color = TealSparkle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
