package com.scrollbattle.data

import android.content.Context
import android.content.SharedPreferences
import com.scrollbattle.domain.model.*
import com.scrollbattle.util.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface ScrollBattleRepository {
    val state: StateFlow<ScrollBattleState>
    fun updateState(transform: (ScrollBattleState) -> ScrollBattleState)
    fun recordReelTransition(source: String = "Instagram")
    fun setDailyLimit(limit: Int)
    fun unlockBonusAllowance(amount: Int = 20)
    fun refreshPermissions(context: Context)
    fun resetChallenge()
    fun selectChallengeOption(optionIndex: Int)
    fun submitChallengeAnswer()
    fun deleteHistory()
    fun resetSettings()
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
    val navigationEvents: kotlinx.coroutines.flow.SharedFlow<String>
    fun triggerNavigation(destination: String)
}

class DefaultScrollBattleRepository(
    private val context: Context? = null
) : ScrollBattleRepository {

    private val prefs: SharedPreferences? =
        context?.getSharedPreferences("scroll_battle_prefs", Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(loadInitialState())
    override val state: StateFlow<ScrollBattleState> = _state.asStateFlow()

    private val _navigationEvents = kotlinx.coroutines.flow.MutableSharedFlow<String>(extraBufferCapacity = 1)
    override val navigationEvents: kotlinx.coroutines.flow.SharedFlow<String> = _navigationEvents

    override fun triggerNavigation(destination: String) {
        _navigationEvents.tryEmit(destination)
    }

    companion object {
        @Volatile
        private var instance: DefaultScrollBattleRepository? = null

        fun getInstance(context: Context): DefaultScrollBattleRepository {
            return instance ?: synchronized(this) {
                instance ?: DefaultScrollBattleRepository(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun loadInitialState(): ScrollBattleState {
        val todayStr = getTodayDateString()
        val defaultState = ScrollBattleState()

        if (prefs == null) return defaultState

        val lastDate = prefs.getString("last_date", "") ?: ""
        val isNewDay = lastDate.isNotEmpty() && lastDate != todayStr

        val baseLimit = 50
        val warningThreshold = prefs.getInt("warning_threshold", defaultState.settings.warningThresholdPercent)
        val hardIntervention = prefs.getBoolean("hard_intervention", defaultState.settings.hardIntervention)

        val todayReels = if (isNewDay) 0 else prefs.getInt("today_reels", defaultState.todayReels)
        val bonusAllowance = if (isNewDay) 0 else prefs.getInt("bonus_allowance", defaultState.bonusAllowance)
        val bonusClaimed = if (isNewDay) false else prefs.getBoolean("bonus_claimed", defaultState.bonusClaimedToday)

        if (isNewDay || lastDate.isEmpty()) {
            prefs.edit().putString("last_date", todayStr).apply()
        }

        val appSettings = defaultState.settings.copy(
            dailyLimit = baseLimit,
            warningThresholdPercent = warningThreshold,
            hardIntervention = hardIntervention
        )

        val initial = defaultState.copy(
            todayReels = todayReels,
            baseAllowance = baseLimit,
            bonusAllowance = bonusAllowance,
            bonusClaimedToday = bonusClaimed,
            settings = appSettings
        )

        return if (context != null) {
            val acc = PermissionUtils.isAccessibilityServiceEnabled(context)
            val usage = PermissionUtils.isUsageAccessGranted(context)
            val overlay = PermissionUtils.canDrawOverlays(context)
            val status = PermissionUtils.computeTrackingStatus(context)
            initial.copy(
                accessibilityGranted = acc,
                usageAccessGranted = usage,
                overlayPermissionGranted = overlay,
                trackingStatus = status
            )
        } else {
            initial
        }
    }

    private fun persistState(newState: ScrollBattleState) {
        prefs?.edit()?.apply {
            putInt("today_reels", newState.todayReels)
            putInt("base_allowance", newState.baseAllowance)
            putInt("bonus_allowance", newState.bonusAllowance)
            putBoolean("bonus_claimed", newState.bonusClaimedToday)
            putInt("daily_limit", newState.settings.dailyLimit)
            putInt("warning_threshold", newState.settings.warningThresholdPercent)
            putBoolean("hard_intervention", newState.settings.hardIntervention)
            putString("last_date", getTodayDateString())
            apply()
        }
        context?.let { com.scrollbattle.widget.BrainPalWidgetProvider.updateAllWidgets(it) }
    }

    override fun updateState(transform: (ScrollBattleState) -> ScrollBattleState) {
        val updated = transform(_state.value)
        _state.value = updated
        persistState(updated)
    }

    private var lastRecordedTimestamp: Long = 0L
    private var lastSessionTimestamp: Long = 0L

    override fun recordReelTransition(source: String) {
        val now = System.currentTimeMillis()
        // Deduplicate reel transitions (700ms for short videos, 150ms for simulated test clicks)
        val minInterval = if (source == "Simulated") 150L else 700L
        if (now - lastRecordedTimestamp < minInterval) {
            return
        }
        lastRecordedTimestamp = now

        val timeSdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        val currentTimeLabel = timeSdf.format(Date(now))

        updateState { current ->
            val newToday = current.todayReels + 1
            val estimatedMinutes = (newToday * 25) / 60

            // Manage real sessions dynamically
            val isNewSession = (now - lastSessionTimestamp > 5 * 60 * 1000L) || current.recentSessions.isEmpty()
            lastSessionTimestamp = now

            val updatedSessions = if (isNewSession) {
                val newSession = ScrollSession(
                    id = "session_${System.currentTimeMillis()}",
                    timeLabel = currentTimeLabel,
                    durationMinutes = 1,
                    reelCount = 1,
                    periodTag = source,
                    paceTag = "Active"
                )
                listOf(newSession) + current.recentSessions.take(9)
            } else {
                val first = current.recentSessions.first()
                val updatedFirst = first.copy(
                    reelCount = first.reelCount + 1,
                    durationMinutes = ((first.reelCount + 1) * 25) / 60 + 1,
                    timeLabel = currentTimeLabel
                )
                listOf(updatedFirst) + current.recentSessions.drop(1)
            }

            // Update current today in weekly list as well
            val updatedWeekly = current.weeklyDays.map { day ->
                if (day.isToday) day.copy(
                    reelsCount = newToday,
                    durationMinutes = estimatedMinutes,
                    sessionCount = updatedSessions.size
                ) else day
            }
            current.copy(
                todayReels = newToday,
                todayDurationMinutes = estimatedMinutes,
                todaySessionsCount = updatedSessions.size,
                recentSessions = updatedSessions,
                weeklyDays = updatedWeekly
            )
        }
    }

    override fun setDailyLimit(limit: Int) {
        updateState { current ->
            current.copy(
                baseAllowance = limit,
                settings = current.settings.copy(dailyLimit = limit)
            )
        }
    }

    override fun unlockBonusAllowance(amount: Int) {
        updateState { current ->
            if (current.bonusClaimedToday) {
                current
            } else {
                current.copy(
                    bonusAllowance = current.bonusAllowance + amount,
                    bonusClaimedToday = true,
                    challengeCompleted = true,
                    challengeFeedback = "Challenge completed! +$amount Reels unlocked."
                )
            }
        }
    }

    override fun refreshPermissions(context: Context) {
        val acc = PermissionUtils.isAccessibilityServiceEnabled(context)
        val usage = PermissionUtils.isUsageAccessGranted(context)
        val overlay = PermissionUtils.canDrawOverlays(context)
        val status = PermissionUtils.computeTrackingStatus(context)

        updateState { current ->
            current.copy(
                accessibilityGranted = acc,
                usageAccessGranted = usage,
                overlayPermissionGranted = overlay,
                trackingStatus = status
            )
        }
    }

    override fun resetChallenge() {
        updateState { current ->
            current.copy(
                activeChallengeQuestionIndex = 0,
                selectedOptionIndex = null,
                challengeFeedback = null,
                challengeMistakes = 0,
                challengeCompleted = false
            )
        }
    }

    override fun selectChallengeOption(optionIndex: Int) {
        updateState { current ->
            current.copy(selectedOptionIndex = optionIndex, challengeFeedback = null)
        }
    }

    override fun submitChallengeAnswer() {
        val current = _state.value
        if (current.challengeCompleted) return
        val selected = current.selectedOptionIndex ?: return
        val currentQ = current.questions.getOrNull(current.activeChallengeQuestionIndex) ?: return

        if (selected == currentQ.correctIndex) {
            val nextIndex = current.activeChallengeQuestionIndex + 1
            if (nextIndex >= current.questions.size) {
                // Completed all 6 questions!
                unlockBonusAllowance(20)
            } else {
                updateState { s ->
                    s.copy(
                        activeChallengeQuestionIndex = nextIndex,
                        selectedOptionIndex = null,
                        challengeFeedback = "Correct! Moving to question ${nextIndex + 1}/6."
                    )
                }
            }
        } else {
            updateState { s ->
                s.copy(
                    challengeMistakes = s.challengeMistakes + 1,
                    challengeFeedback = "Incorrect answer. Try again."
                )
            }
        }
    }

    override fun deleteHistory() {
        updateState { current ->
            current.copy(
                todayReels = 0,
                todayDurationMinutes = 0,
                todaySessionsCount = 0,
                recentSessions = emptyList(),
                weeklyDays = current.weeklyDays.map { it.copy(reelsCount = 0, durationMinutes = 0, sessionCount = 0) }
            )
        }
    }

    override fun resetSettings() {
        updateState { current ->
            current.copy(
                baseAllowance = 50,
                settings = AppSettings()
            )
        }
    }

    override fun isOnboardingCompleted(): Boolean {
        return prefs?.getBoolean("onboarding_completed", false) ?: false
    }

    override fun setOnboardingCompleted(completed: Boolean) {
        prefs?.edit()?.putBoolean("onboarding_completed", completed)?.apply()
    }
}
