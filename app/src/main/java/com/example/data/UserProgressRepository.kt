package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProgressRepository(private val dao: UserProgressDao) {

    // Helper map for badge titles and descriptions (for reference or UI)
    companion object {
        const val BADGE_INTRO = "INTRO_MASTER"
        const val BADGE_TRAINER = "ROBOT_TRAINER"
        const val BADGE_GENAI = "AI_CREATOR"
        const val BADGE_ASKER = "CURIOSITY_SPARK"
    }

    // Get the reactive user progress flow
    val progressFlow: Flow<UserProgress> = dao.getUserProgress().map { it ?: UserProgress() }

    private suspend fun getOrCreateProgress(): UserProgress {
        return dao.getProgressImmediate() ?: UserProgress()
    }

    suspend fun addXp(amount: Int) {
        val current = getOrCreateProgress()
        val newXp = current.xp + amount
        // Calculation: 100 XP per level (level 1 is 0-99 XP, level 2 is 100-199, etc.)
        val newLevel = (newXp / 100) + 1
        val updated = current.copy(
            xp = newXp,
            level = if (newLevel > current.level) newLevel else current.level
        )
        dao.insertOrUpdateProgress(updated)
    }

    suspend fun completeLesson(lessonId: String) {
        val current = getOrCreateProgress()
        val completed = current.completedLessons.split(",").filter { it.isNotEmpty() }.toMutableSet()
        if (completed.add(lessonId)) {
            // New lesson completed!
            val newCompletedString = completed.joinToString(",")
            val badgeIdToUnlock = when (lessonId) {
                "intro" -> BADGE_INTRO
                "genai" -> BADGE_GENAI
                else -> null
            }

            var unlocked = current.unlockedBadges.split(",").filter { it.isNotEmpty() }.toMutableSet()
            if (badgeIdToUnlock != null) {
                unlocked.add(badgeIdToUnlock)
            }

            val updated = current.copy(
                completedLessons = newCompletedString,
                unlockedBadges = unlocked.joinToString(",")
            )
            dao.insertOrUpdateProgress(updated)
            addXp(50) // Award 50 XP for completing a lesson!
        }
    }

    suspend fun updateRobotHighscore(score: Int) {
        val current = getOrCreateProgress()
        var unlocked = current.unlockedBadges.split(",").filter { it.isNotEmpty() }.toMutableSet()
        
        // Unlock Robot Trainer badge if they got a streak of 5 or more!
        if (score >= 5) {
            unlocked.add(BADGE_TRAINER)
        }

        val updated = current.copy(
            robotHighscore = if (score > current.robotHighscore) score else current.robotHighscore,
            unlockedBadges = unlocked.joinToString(",")
        )
        dao.insertOrUpdateProgress(updated)
        // Award XP equal to the score times 10 for training
        addXp(score * 10)
    }

    suspend fun incrementSandboxStory() {
        val current = getOrCreateProgress()
        val nextCount = current.sandboxStoryCount + 1
        var unlocked = current.unlockedBadges.split(",").filter { it.isNotEmpty() }.toMutableSet()
        
        // Unlock storyteller or curiosity spark badge!
        if (nextCount >= 3) {
            unlocked.add(BADGE_ASKER)
        }

        val updated = current.copy(
            sandboxStoryCount = nextCount,
            unlockedBadges = unlocked.joinToString(",")
        )
        dao.insertOrUpdateProgress(updated)
        addXp(30) // Gaining 30 XP for experimenting with prompt engineering!
    }
}
