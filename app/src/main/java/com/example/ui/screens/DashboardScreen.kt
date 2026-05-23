package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProgress
import com.example.ui.AppScreen
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    progress: UserProgress,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    var animateCards by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animateCards = true
    }

    // Dynamic polished background for sleek light theme
    val lightBackgroundBrush = Brush.verticalGradient(
        colors = listOf(
            GalacticBackground,
            Color(0xFFFBF8FD),
            Color(0xFFF5EEFA)
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(lightBackgroundBrush)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Sleek Explorer Welcome Header ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ADVENTURE MAP",
                        color = SoftLavender,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Hi, Explorer! 👋",
                        color = Color(0xFF201A1B),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                // Sleek M3 Profile Avatar Container
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .shadow(2.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFF2B8B5))
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "E",
                        color = Color(0xFF601410),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // --- 2. Current Mission Card Display ---
        item {
            val xpInCurrentLevel = progress.xp % 100
            val animatedProgress by animateFloatAsState(
                targetValue = xpInCurrentLevel / 100f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                label = "xp_bar"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("welcome_card"),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEADDFF)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD0BCFF).copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Mission Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            // Current Mission Tag Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(Color(0xFF6750A4))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "CURRENT MISSION",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "The Robot's Brain (Level ${progress.level})",
                                color = Color(0xFF21005D),
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Text(
                                text = "Learn how AI looks at pictures & labels codes!",
                                color = Color(0xFF21005D).copy(alpha = 0.7f),
                                fontSize = 13.sp,
                                lineHeight = 17.sp
                            )
                        }

                        // Cute Robot icon background bubble
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White.copy(alpha = 0.45f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🤖",
                                fontSize = 28.sp
                            )
                        }
                    }

                    // Progress indicators
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            color = Color(0xFF6750A4),
                            trackColor = Color.White.copy(alpha = 0.5f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${xpInCurrentLevel}% to Level ${progress.level + 1}",
                                color = Color(0xFF21005D),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$xpInCurrentLevel/100 XP",
                                color = Color(0xFF21005D),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Continue learning direct button
                    Button(
                        onClick = { onNavigate(AppScreen.LESSON_LIST) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF21005D),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Continue Learning",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // --- 2. Interactive Quest Blocks (Grid-style Cards) ---
        item {
            Text(
                text = "My Learning Quests 🪄",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = OnSurfaceText,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
        }

        item {
            AnimatedVisibility(
                visible = animateCards,
                enter = fadeIn(animationSpec = spring()) + slideInVertically(
                    initialOffsetY = { 60 },
                    animationSpec = spring()
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    ActivityQuestCard(
                        title = "Visual AI Storybook 📖",
                        description = "Learn what AI is, how computers read items, and paint with codes!",
                        color = PurpleSpaceDefault,
                        characterIcon = "🤖",
                        tag = "visual_lessons_card",
                        onClick = { onNavigate(AppScreen.LESSON_LIST) }
                    )

                    ActivityQuestCard(
                        title = "Sparky's Brain Trainer 🎯",
                        description = "Play matching games to train a kid-robot model. Complete 100% accuracy!",
                        color = TealSparkle,
                        characterIcon = "🐶",
                        subtext = "High Score: ${progress.robotHighscore} Streak",
                        tag = "robot_train_card",
                        onClick = { onNavigate(AppScreen.ROBOT_TRAIN_GAME) }
                    )

                    ActivityQuestCard(
                        title = "Penny's Creative Lab 🪄",
                        description = "Pick spell words & generate imaginary stories instantly using AI!",
                        color = CoralPink,
                        characterIcon = "🎨",
                        subtext = "Stories Made: ${progress.sandboxStoryCount}",
                        tag = "sandbox_lab_card",
                        onClick = { onNavigate(AppScreen.GENAI_SANDBOX) }
                    )

                    ActivityQuestCard(
                        title = "Ask Robo-Tutor 💬",
                        description = "Sparky explains anything! Does AI sleep? Can robots laugh?",
                        color = GoldenSun,
                        characterIcon = "🧠",
                        tag = "robo_tutor_card",
                        onClick = { onNavigate(AppScreen.ROBO_TUTOR) }
                    )
                }
            }
        }

        // --- 3. Badges Gallery (Show unlocked rewards) ---
        item {
            Text(
                text = "My Space Badges 🎖️",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = OnSurfaceText,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BadgeWidget(
                            unlocked = progress.isBadgeUnlocked("INTRO_MASTER"),
                            emoji = "🤖",
                            label = "Intro Master",
                            hint = "Read 'What is AI'"
                        )
                        BadgeWidget(
                            unlocked = progress.isBadgeUnlocked("ROBOT_TRAINER"),
                            emoji = "🎯",
                            label = "Super Trainer",
                            hint = "Get a game streak of 5"
                        )
                        BadgeWidget(
                            unlocked = progress.isBadgeUnlocked("AI_CREATOR"),
                            emoji = "🎨",
                            label = "Creator Star",
                            hint = "Create GenAI stories"
                        )
                        BadgeWidget(
                            unlocked = progress.isBadgeUnlocked("CURIOSITY_SPARK"),
                            emoji = "🪄",
                            label = "Curio Explorer",
                            hint = "Make 3 Lab stories"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityQuestCard(
    title: String,
    description: String,
    color: Color,
    characterIcon: String,
    modifier: Modifier = Modifier,
    subtext: String? = null,
    tag: String = "",
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(tag)
            .clickable(onClick = onClick)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(colors = listOf(color, color.copy(alpha = 0.5f))),
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CosmicSurface.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Massive Emojis
            Text(
                text = characterIcon,
                fontSize = 44.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            // Texts
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = SoftLavender,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                if (subtext != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtext,
                        color = GoldenSun,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Arrow Icon
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f))
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start activity",
                    tint = color
                )
            }
        }
    }
}

@Composable
fun BadgeWidget(
    unlocked: Boolean,
    emoji: String,
    label: String,
    hint: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(76.dp)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(if (unlocked) 3.dp else 0.dp, CircleShape)
                .clip(CircleShape)
                .background(if (unlocked) Color(0xFFFDE293) else Color(0xFFE2E8F0))
                .border(
                    width = 2.dp,
                    color = if (unlocked) Color(0xFFEAC453) else Color(0xFFCBD5E1),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 26.sp,
                modifier = Modifier.alpha(if (unlocked) 1.0f else 0.35f)
            )

            if (!unlocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked badge",
                    tint = Color(0xFF64748B),
                    modifier = Modifier
                        .size(13.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 1.dp, y = 1.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = if (unlocked) Color(0xFF221B00) else Color(0xFF64748B),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = hint,
            color = Color(0xFF64748B).copy(alpha = 0.85f),
            fontSize = 8.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp,
            maxLines = 2
        )
    }
}
