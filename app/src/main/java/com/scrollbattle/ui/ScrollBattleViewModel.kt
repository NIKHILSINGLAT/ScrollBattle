package com.scrollbattle.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scrollbattle.data.DefaultScrollBattleRepository
import com.scrollbattle.data.ScrollBattleRepository
import com.scrollbattle.domain.model.ScrollBattleState
import com.scrollbattle.domain.model.TrackingStatus
import kotlinx.coroutines.flow.StateFlow

class ScrollBattleViewModel(
    private val repository: ScrollBattleRepository = DefaultScrollBattleRepository()
) : ViewModel() {

    val uiState: StateFlow<ScrollBattleState> = repository.state
    val navigationEvents: kotlinx.coroutines.flow.SharedFlow<String> = repository.navigationEvents

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ScrollBattleViewModel(DefaultScrollBattleRepository.getInstance(context)) as T
                }
            }
    }

    fun setDailyLimit(limit: Int) {
        repository.setDailyLimit(limit)
    }

    fun setWarningThreshold(percent: Int) {
        repository.updateState { current ->
            current.copy(
                settings = current.settings.copy(warningThresholdPercent = percent)
            )
        }
    }

    fun toggleHardIntervention(enabled: Boolean) {
        repository.updateState { current ->
            current.copy(
                settings = current.settings.copy(hardIntervention = enabled)
            )
        }
    }

    fun selectAnalyticsDay(index: Int) {
        repository.updateState { current ->
            current.copy(selectedAnalyticsDayIndex = index)
        }
    }

    fun setAnalyticsTimeSpan(timeSpan: String) {
        repository.updateState { current ->
            current.copy(analyticsTimeSpan = timeSpan)
        }
    }

    fun selectChallengeOption(optionIndex: Int) {
        repository.selectChallengeOption(optionIndex)
    }

    fun submitChallengeAnswer() {
        repository.submitChallengeAnswer()
    }

    fun resetChallenge() {
        repository.resetChallenge()
    }

    fun deleteHistory() {
        repository.deleteHistory()
    }

    fun resetSettings() {
        repository.resetSettings()
    }

    fun setTrackingStatus(status: TrackingStatus) {
        repository.updateState { current ->
            current.copy(trackingStatus = status)
        }
    }

    fun incrementScrollCount(amount: Int = 1) {
        repository.updateState { current ->
            current.copy(todayReels = current.todayReels + amount)
        }
    }

    fun simulateReelScroll() {
        repository.recordReelTransition("Simulated")
    }

    fun refreshPermissions(context: Context) {
        repository.refreshPermissions(context)
    }

    fun isOnboardingCompleted(): Boolean = repository.isOnboardingCompleted()

    fun completeOnboarding() {
        repository.setOnboardingCompleted(true)
    }
}
