package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.api.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

// Screen states
enum class AppScreen {
    DASHBOARD,
    LESSON_LIST,
    LESSON_DETAIL,
    ROBOT_TRAIN_GAME,
    GENAI_SANDBOX,
    ROBO_TUTOR
}

// Labeled item for supervised learning training game
data class TrainingItem(
    val name: String,
    val emoji: String,
    val category: String, // "Food", "Vehicle", "Animal"
    val funFact: String
)

class AiAcademyViewModel(
    application: Application,
    private val repository: UserProgressRepository
) : AndroidViewModel(application) {

    // Current Screen
    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Active Lesson (if in detail screen)
    private val _activeLesson = MutableStateFlow<Lesson?>(null)
    val activeLesson: StateFlow<Lesson?> = _activeLesson.asStateFlow()

    // Active Lesson Step Index
    private val _activeLessonStepIndex = MutableStateFlow(0)
    val activeLessonStepIndex: StateFlow<Int> = _activeLessonStepIndex.asStateFlow()

    // User progress flowing from database
    val userProgress: StateFlow<UserProgress> = repository.progressFlow
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = UserProgress()
        )

    // --- ROBOT TRAINING GAME STATE ---
    val robotGameCategories = listOf("Food", "Vehicle", "Animal")
    private val trainingItems = listOf(
        TrainingItem("Apple", "🍎", "Food", "Apples have fiber and keep the robot's gears clean!"),
        TrainingItem("Banana", "🍌", "Food", "Bananas give organic potassium energy!"),
        TrainingItem("Pizza", "🍕", "Food", "Pizza has cheese triangles! The robot loves shape theory!"),
        TrainingItem("Broccoli", "🥦", "Food", "Tiny green trees full of vitamins!"),
        TrainingItem("Avocado", "🥑", "Food", "Healthy fats that make robot joints glide smoothly!"),
        TrainingItem("Car", "🚗", "Vehicle", "Cars roll on 4 wheels to transport humans!"),
        TrainingItem("Airplane", "✈️", "Vehicle", "Airplanes fly in the sky using aerodynamics!"),
        TrainingItem("Rocket", "🚀", "Vehicle", "Rockets use intense thrust to break through physical orbit!"),
        TrainingItem("Bicycle", "🚲", "Vehicle", "Bicycles run on pure leg power! Go green!"),
        TrainingItem("Train", "🚂", "Vehicle", "Trains track across steel paths carrying heavy cargo!"),
        TrainingItem("Dog", "🐶", "Animal", "Dogs are loyal companions that wag their tails when happy!"),
        TrainingItem("Lion", "🦁", "Animal", "Lions roar as the brave kings of the savannah!"),
        TrainingItem("Elephant", "🐘", "Animal", "Elephants have huge trunks and never forget their files!"),
        TrainingItem("Whale", "🐳", "Animal", "Whales sing melodic ocean symphonies!"),
        TrainingItem("Frog", "🐸", "Animal", "Frogs leap high using flexible target muscles!")
    )

    private val _currentTrainingItem = MutableStateFlow(trainingItems.first())
    val currentTrainingItem: StateFlow<TrainingItem> = _currentTrainingItem.asStateFlow()

    private val _robotGameConfidence = MutableStateFlow(10) // starting at 10%
    val robotGameConfidence: StateFlow<Int> = _robotGameConfidence.asStateFlow()

    private val _robotGameStreak = MutableStateFlow(0)
    val robotGameStreak: StateFlow<Int> = _robotGameStreak.asStateFlow()

    private val _robotFeedbackMsg = MutableStateFlow("Hi! I'm Sparky! Can you help me sort items so I can learn to classify them?")
    val robotFeedbackMsg: StateFlow<String> = _robotFeedbackMsg.asStateFlow()

    private val _isRobotHappy = MutableStateFlow<Boolean?>(null) // true = happy, false = confused, null = idle
    val isRobotHappy: StateFlow<Boolean?> = _isRobotHappy.asStateFlow()

    // --- GENAI SANDBOX STATE ---
    val sandboxCharacters = listOf("A baby T-Rex 🦖", "A flying dolphin 🐬", "A detective raccoon 🦝", "A super puppy with laser eyes 🐶")
    val sandboxActions = listOf("wearing a bubble-gum armor cape", "riding an electric lightning skateboard 🛹", "baking cosmic chocolate cookies 🍪", "playing space jazz on an accordion 🪗")
    val sandboxPlaces = listOf("on a moon made of sweet candy floss ☁️", "under high-tech neon waterfalls 🌊", "inside a flying robot castle 🏰", "climbing a glowing emerald volcano 🌋")

    private val _selectedCharacterIndex = MutableStateFlow(0)
    val selectedCharacterIndex: StateFlow<Int> = _selectedCharacterIndex.asStateFlow()

    private val _selectedActionIndex = MutableStateFlow(0)
    val selectedActionIndex: StateFlow<Int> = _selectedActionIndex.asStateFlow()

    private val _selectedPlaceIndex = MutableStateFlow(0)
    val selectedPlaceIndex: StateFlow<Int> = _selectedPlaceIndex.asStateFlow()

    private val _customPromptText = MutableStateFlow("")
    val customPromptText: StateFlow<String> = _customPromptText.asStateFlow()

    private val _sandboxStory = MutableStateFlow<String?>(null)
    val sandboxStory: StateFlow<String?> = _sandboxStory.asStateFlow()

    private val _isStoryLoading = MutableStateFlow(false)
    val isStoryLoading: StateFlow<Boolean> = _isStoryLoading.asStateFlow()

    // --- ROBO-TUTOR STATE ---
    val suggestedQuestions = listOf(
        "Does AI have feelings?",
        "Can a computer paint real art?",
        "Will robots take over the world?",
        "How is AI different from human brains?"
    )

    private val _tutorQuestion = MutableStateFlow("")
    val tutorQuestion: StateFlow<String> = _tutorQuestion.asStateFlow()

    private val _tutorAnswer = MutableStateFlow<String?>(null)
    val tutorAnswer: StateFlow<String?> = _tutorAnswer.asStateFlow()

    private val _isTutorLoading = MutableStateFlow(false)
    val isTutorLoading: StateFlow<Boolean> = _isTutorLoading.asStateFlow()


    // Navigation Control
    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Set Active Lesson and reset steps
    fun startLesson(lesson: Lesson) {
        _activeLesson.value = lesson
        _activeLessonStepIndex.value = 0
        navigateTo(AppScreen.LESSON_DETAIL)
    }

    fun nextLessonStep() {
        val lesson = _activeLesson.value ?: return
        if (_activeLessonStepIndex.value < lesson.steps.size - 1) {
            _activeLessonStepIndex.value++
            // Give 5 XP just for reading educational progress pages!
            viewModelScope.launch {
                repository.addXp(5)
            }
        } else {
            // Completed the whole lesson!
            viewModelScope.launch {
                repository.completeLesson(lesson.id)
                // Go back to lesson selection
                navigateTo(AppScreen.LESSON_LIST)
            }
        }
    }

    fun prevLessonStep() {
        if (_activeLessonStepIndex.value > 0) {
            _activeLessonStepIndex.value--
        }
    }

    // --- ROBOT TRAINING GAME ACTION ---
    fun submitTrainingClassification(selectedCategory: String) {
        val currentItem = _currentTrainingItem.value
        val isCorrect = selectedCategory == currentItem.category

        if (isCorrect) {
            val nextConfidence = (_robotGameConfidence.value + 15).coerceAtMost(100)
            _robotGameConfidence.value = nextConfidence
            _robotGameStreak.value++
            _isRobotHappy.value = true

            if (nextConfidence >= 100) {
                // Completed training!
                _robotFeedbackMsg.value = "Hooray! Training complete! You successfully trained me to a model accuracy of 100%! I am now a certified smartest robot! 🎓 +100 XP"
                viewModelScope.launch {
                    repository.updateRobotHighscore(_robotGameStreak.value)
                    // Reset game settings
                    _robotGameConfidence.value = 15
                }
            } else {
                _robotFeedbackMsg.value = "Splendid! Labeled successfully! Yes, $currentItem.emoji ${currentItem.name} is a ${currentItem.category}. $currentItem.funFact Confidence reaches ${nextConfidence}%! Accuracy increases!"
                selectNextTrainingItem()
            }
        } else {
            // Misclassification (Noise / Error / Loss function adjustment)
            _robotGameStreak.value = 0
            val nextConfidence = (_robotGameConfidence.value - 10).coerceAtLeast(10)
            _robotGameConfidence.value = nextConfidence
            _isRobotHappy.value = false
            _robotFeedbackMsg.value = "Whoops! Machine learning error! I classified $currentItem.emoji ${currentItem.name} as a $selectedCategory, but my neural networks detected that's wrong. Let's adjust weights! Confidence drops to ${nextConfidence}%."
            selectNextTrainingItem()
        }
    }

    private fun selectNextTrainingItem() {
        val current = _currentTrainingItem.value
        var nextItem = trainingItems.random()
        while (nextItem.name == current.name) {
            nextItem = trainingItems.random()
        }
        _currentTrainingItem.value = nextItem
    }

    fun resetRobotTrainingGame() {
        _robotGameConfidence.value = 10
        _robotGameStreak.value = 0
        _isRobotHappy.value = null
        _robotFeedbackMsg.value = "Let's kickstart our neural model training! Sort the popping items to help Sparky understand categories!"
        selectNextTrainingItem()
    }

    // --- GENAI SANDBOX GAMES ACTIONS ---
    fun selectSandboxCharacter(index: Int) {
        _selectedCharacterIndex.value = index
    }

    fun selectSandboxAction(index: Int) {
        _selectedActionIndex.value = index
    }

    fun selectSandboxPlace(index: Int) {
        _selectedPlaceIndex.value = index
    }

    fun updateCustomPrompt(text: String) {
        _customPromptText.value = text
    }

    fun generateSandboxCreation(isCustomMode: Boolean) {
        val prompt = if (isCustomMode) {
            _customPromptText.value
        } else {
            "An imaginary story about " +
                    sandboxCharacters[_selectedCharacterIndex.value] + " " +
                    sandboxActions[_selectedActionIndex.value] + " " +
                    sandboxPlaces[_selectedPlaceIndex.value]
        }

        if (prompt.trim().isEmpty()) return

        _isStoryLoading.value = true
        _sandboxStory.value = null

        viewModelScope.launch {
            val systemInstruction = "You are Paint Penny, an AI companion artist in the AI Academy app. " +
                    "The user is a child who built this prompt: '$prompt'. " +
                    "Write a super cheerful, delightful, and highly descriptive fairy-tale story based on it in EXACTLY 3 sentences. " +
                    "Use emojis extensively! Keep nouns vivid and colors bright. " +
                    "At the bottom, on a completely new line, add exactly: " +
                    "'Penny's Art Idea: ⭐ [Write a suggestion of a funny detail to paint next, like: Let's paint the dinosaur with a pink mustache!]'"

            val response = RetrofitClient.getKidFriendlyResponse(prompt, systemInstruction)
            _sandboxStory.value = response
            _isStoryLoading.value = false

            // Complete progress in database
            repository.incrementSandboxStory()
        }
    }

    fun clearSandbox() {
        _customPromptText.value = ""
        _sandboxStory.value = null
    }

    // --- ROBO-TUTOR VIEWS ACTIONS ---
    fun setTutorQuestion(text: String) {
        _tutorQuestion.value = text
    }

    fun askRobotTutor() {
        val question = _tutorQuestion.value
        if (question.trim().isEmpty()) return

        _isTutorLoading.value = true
        _tutorAnswer.value = null

        viewModelScope.launch {
            val systemInstruction = "You are Sparky, an AI Academy tutor. " +
                    "The user is a curious child asking this question: '$question'. " +
                    "Answer in an extremely kid-friendly, playful, and easy-to-understand manner. " +
                    "Limit response strictly to 3 sentences. " +
                    "Provide a relatable analogy, keep it encouraging and secure, and sprinkle relevant emojis! " +
                    "Explain technical jargon in direct words (like 'neural network means computer brain cells connecting')."

            val response = RetrofitClient.getKidFriendlyResponse(question, systemInstruction)
            _tutorAnswer.value = response
            _isTutorLoading.value = false
            repository.addXp(15) // XP for asking questions!
        }
    }
}

class AiAcademyViewModelFactory(
    private val application: Application,
    private val repository: UserProgressRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AiAcademyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AiAcademyViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
