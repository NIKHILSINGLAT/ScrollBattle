package com.scrollbattle.ui

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ScrollBattleViewModelTest {

    @Test
    fun initialState_hasCorrectDefaults() = runTest {
        val viewModel = ScrollBattleViewModel()
        val state = viewModel.uiState.first()

        assertEquals(0, state.todayReels)
        assertEquals(50, state.baseAllowance)
        assertEquals(0, state.bonusAllowance)
        assertEquals(50, state.totalAllowance)
        assertEquals(50, state.remainingReels)
        assertEquals(0, state.percentageUsed)
    }

    @Test
    fun setDailyLimit_updatesAllowanceAndRemaining() = runTest {
        val viewModel = ScrollBattleViewModel()
        viewModel.setDailyLimit(75)
        val state = viewModel.uiState.first()

        assertEquals(75, state.baseAllowance)
        assertEquals(75, state.totalAllowance)
        assertEquals(75, state.remainingReels)
    }

    @Test
    fun mathsChallenge_correctSubmission_grantsBonusOnce() = runTest {
        val viewModel = ScrollBattleViewModel()

        // Solve all 6 questions correctly
        for (i in 0 until 6) {
            val q = viewModel.uiState.value.questions[i]
            viewModel.selectChallengeOption(q.correctIndex)
            viewModel.submitChallengeAnswer()
        }

        val state = viewModel.uiState.first()
        assertTrue(state.challengeCompleted)
        assertTrue(state.bonusClaimedToday)
        assertEquals(20, state.bonusAllowance)
        assertEquals(70, state.totalAllowance)

        // Try submitting again; should not grant bonus repeatedly
        viewModel.submitChallengeAnswer()
        assertEquals(20, viewModel.uiState.first().bonusAllowance)
    }

    @Test
    fun deleteHistory_resetsCountersWithoutTouchingSettings() = runTest {
        val viewModel = ScrollBattleViewModel()
        viewModel.setDailyLimit(75)
        viewModel.deleteHistory()

        val state = viewModel.uiState.first()
        assertEquals(0, state.todayReels)
        assertEquals(0, state.todayDurationMinutes)
        assertEquals(0, state.recentSessions.size)
        assertEquals(75, state.baseAllowance) // preserved setting
    }

    @Test
    fun resetSettings_restoresDefaultAllowanceWithoutDeletingSessions() = runTest {
        val viewModel = ScrollBattleViewModel()
        viewModel.simulateReelScroll()
        viewModel.setDailyLimit(100)
        assertEquals(100, viewModel.uiState.first().baseAllowance)

        viewModel.resetSettings()
        val state = viewModel.uiState.first()
        assertEquals(50, state.baseAllowance)
        assertEquals(1, state.todayReels) // preserved real tracked reels
    }
}
