package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridOn
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
fun SandboxScreen(
    viewModel: AiAcademyViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCustomMode by remember { mutableStateOf(false) }

    val charIndex by viewModel.selectedCharacterIndex.collectAsState()
    val actIndex by viewModel.selectedActionIndex.collectAsState()
    val placeIndex by viewModel.selectedPlaceIndex.collectAsState()
    val customPrompt by viewModel.customPromptText.collectAsState()

    val story by viewModel.sandboxStory.collectAsState()
    val isLoading by viewModel.isStoryLoading.collectAsState()

    val completedStoryCount by viewModel.userProgress.collectAsState().let {
        derivedStateOf { it.value.sandboxStoryCount }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Penny's Creative Lab 🪄", fontWeight = FontWeight.Black) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("sandbox_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isCustomMode = !isCustomMode
                            viewModel.clearSandbox()
                        },
                        modifier = Modifier.testTag("toggle_custom_mode_button")
                    ) {
                        Icon(
                            imageVector = if (isCustomMode) Icons.Default.GridOn else Icons.Default.Edit,
                            contentDescription = "Toggle Custom Input Mode"
                        )
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Welcome Header Banner ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🎨", fontSize = 42.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Meet Paint Penny!",
                            color = NeonPink,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Penny is drawing imaginary cartoon stories based on spells you build! Tap block elements to design a magical prompt, then execute!",
                            color = SoftLavender,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // --- Toggleable Prompts Panel ---
            if (!isCustomMode) {
                // Pill Block Builder Mode
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "1. Pick a character 🦖",
                        color = OnSurfaceText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(viewModel.sandboxCharacters) { idx, item ->
                            val selected = idx == charIndex
                            PromptBlockPill(
                                text = item,
                                selected = selected,
                                onClick = { viewModel.selectSandboxCharacter(idx) }
                            )
                        }
                    }

                    Text(
                        text = "2. Pick an action 🛹",
                        color = OnSurfaceText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(viewModel.sandboxActions) { idx, item ->
                            val selected = idx == actIndex
                            PromptBlockPill(
                                text = item,
                                selected = selected,
                                onClick = { viewModel.selectSandboxAction(idx) }
                            )
                        }
                    }

                    Text(
                        text = "3. Pick a cosmic setting 🏰",
                        color = OnSurfaceText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(viewModel.sandboxPlaces) { idx, item ->
                            val selected = idx == placeIndex
                            PromptBlockPill(
                                text = item,
                                selected = selected,
                                onClick = { viewModel.selectSandboxPlace(idx) }
                            )
                        }
                    }
                }
            } else {
                // Free text customization mode
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Write your Custom Magic Spell! 🪄",
                        color = NeonPink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = customPrompt,
                        onValueChange = { viewModel.updateCustomPrompt(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("custom_prompt_input"),
                        placeholder = { Text("Example: A fat hamster playing basketball in the clouds...", color = Color(0xFF64748B)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPink,
                            unfocusedBorderColor = DarkCardAccent,
                            focusedContainerColor = CosmicSurface,
                            unfocusedContainerColor = CosmicSurface,
                            focusedTextColor = OnSurfaceText,
                            unfocusedTextColor = OnSurfaceText
                        ),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 4
                    )
                }
            }

            // --- Realtime active prompt display box ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardAccent)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "YOUR MAGICAL PROMPT:",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val previewText = if (isCustomMode) {
                        customPrompt.ifEmpty { "Write something interesting above! ✨" }
                    } else {
                        "A story about ${viewModel.sandboxCharacters[charIndex]} ${viewModel.sandboxActions[actIndex]} ${viewModel.sandboxPlaces[placeIndex]}."
                    }
                    Text(
                        text = previewText,
                        color = Color(0xFF21005D),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // --- Magic Generation Button ---
            Button(
                onClick = { viewModel.generateSandboxCreation(isCustomMode) },
                enabled = !isLoading && (if (isCustomMode) customPrompt.isNotEmpty() else true),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("magic_generate_button"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurpleSpaceDefault)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = GoldenSun)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create with Kid AI! 🧠✨",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            // --- LOADING & STORY RESULTS DISPLAY ---
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = NeonPink)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Penny is mixing high-resolution paint colors...\nGenerative AI is brewing a story... 🎨📖",
                            color = SoftLavender,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                story != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Title/Header
                        Text(
                            text = "Penny's Masterpiece Story 📖✨",
                            color = NeonPink,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Main Story Content
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 2.dp, color = NeonPink.copy(alpha = 0.5f), shape = RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = CosmicSurface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = story!!,
                                    color = OnSurfaceText,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Completed creations counter decoration
                        Text(
                            text = "⭐ Magic stories created by you: $completedStoryCount Stories (+30 XP per creation!)",
                            color = GoldenSun,
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

@Composable
fun PromptBlockPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) NeonPink else CosmicSurface)
            .border(
                width = 2.dp,
                color = if (selected) Color.White else DarkCardAccent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else SoftLavender,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}
