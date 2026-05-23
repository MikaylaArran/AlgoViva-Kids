package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val xp: Int = 0,
    val level: Int = 1,
    val robotHighscore: Int = 0,
    val unlockedBadges: String = "", // e.g., "INTRO_MASTER,TRAINER_STAR,GENAI_CREATOR"
    val completedLessons: String = "", // e.g., "intro,supervised,generative"
    val sandboxStoryCount: Int = 0
) {
    // Helper to check if a lesson is completed
    fun isLessonCompleted(lessonId: String): Boolean {
        return completedLessons.split(",").contains(lessonId)
    }

    // Helper to check if a badge is unlocked
    fun isBadgeUnlocked(badgeId: String): Boolean {
        return unlockedBadges.split(",").contains(badgeId)
    }
}
