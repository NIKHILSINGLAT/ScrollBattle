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
        return sdf.format(Date(System.currentTimeMillis() - 4 * 60 * 60 * 1000L))
    }

    private fun readRecentSessions(): List<ScrollSession> {
        val raw = prefs?.getString("recent_sessions", null) ?: return emptyList()
        return try {
            val array = org.json.JSONArray(raw)
            val sessions = mutableListOf<ScrollSession>()
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                sessions.add(ScrollSession(
                    id = item.optString("id"),
                    timeLabel = item.optString("timeLabel"),
                    durationMinutes = item.optInt("durationMinutes"),
                    reelCount = item.optInt("reelCount"),
                    periodTag = item.optString("periodTag"),
                    paceTag = item.optString("paceTag")
                ))
            }
            sessions
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun serializeRecentSessions(sessions: List<ScrollSession>): String {
        val array = org.json.JSONArray()
        sessions.forEach { session ->
            array.put(org.json.JSONObject()
                .put("id", session.id)
                .put("timeLabel", session.timeLabel)
                .put("durationMinutes", session.durationMinutes)
                .put("reelCount", session.reelCount)
                .put("periodTag", session.periodTag)
                .put("paceTag", session.paceTag))
        }
        return array.toString()
    }

    private fun loadInitialState(): ScrollBattleState {
        val todayStr = getTodayDateString()
        val defaultState = ScrollBattleState()

        if (prefs == null) return defaultState

        val lastDate = prefs.getString("last_date", "") ?: ""
        val isNewDay = lastDate.isNotEmpty() && lastDate != todayStr

        val baseLimit = prefs.getInt("daily_limit", prefs.getInt("base_allowance", 50)).coerceIn(1, 500)
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
            todayDurationMinutes = if (isNewDay) 0 else prefs.getInt("today_duration_minutes", 0),
            todaySessionsCount = if (isNewDay) 0 else prefs.getInt("today_sessions_count", 0),
            recentSessions = if (isNewDay) emptyList() else readRecentSessions(),
            baseAllowance = baseLimit,
            bonusAllowance = 0,
            bonusClaimedToday = false,
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
            putInt("today_duration_minutes", newState.todayDurationMinutes)
            putInt("today_sessions_count", newState.todaySessionsCount)
            putString("recent_sessions", serializeRecentSessions(newState.recentSessions))
            putInt("base_allowance", newState.baseAllowance)
            putInt("bonus_allowance", newState.bonusAllowance)
            putBoolean("bonus_claimed", newState.bonusClaimedToday)
            putInt("daily_limit", newState.settings.dailyLimit)
            putInt("warning_threshold", newState.settings.warningThresholdPercent)
            putBoolean("hard_intervention", newState.settings.hardIntervention)
            apply()
        }
        context?.let { com.scrollbattle.widget.BrainPalWidgetProvider.updateAllWidgets(it) }
    }

    override fun updateState(transform: (ScrollBattleState) -> ScrollBattleState) {
        val today = getTodayDateString()
        val storedDate = prefs?.getString("last_date", today) ?: today
        val startingState = if (storedDate != today) {
            lastSessionTimestamp = 0L
            lastRecordedTimestamp = 0L
            _state.value.copy(
                todayReels = 0,
                todayDurationMinutes = 0,
                todaySessionsCount = 0,
                recentSessions = emptyList(),
                bonusAllowance = 0,
                bonusClaimedToday = false,
                challengeCompleted = false,
                activeChallengeQuestionIndex = 0,
                selectedOptionIndex = null,
                challengeFeedback = null,
                challengeMistakes = 0,
                weeklyDays = _state.value.weeklyDays.map { day ->
                    if (day.isToday) day.copy(
                        reelsCount = 0,
                        durationMinutes = 0,
                        sessionCount = 0,
                        status = "Within Limit"
                    ) else day
                }
            )
        } else {
            _state.value
        }
        val updated = transform(startingState)
        _state.value = updated
        persistState(updated)
        prefs?.edit()?.putString("last_date", today)?.apply()
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
                todaySessionsCount = current.todaySessionsCount + if (isNewSession) 1 else 0,
                recentSessions = updatedSessions,
                weeklyDays = updatedWeekly
            )
        }
    }

    override fun setDailyLimit(limit: Int) {
        val safeLimit = limit.coerceIn(1, 500)
        updateState { current ->
            current.copy(
                baseAllowance = safeLimit,
                settings = current.settings.copy(dailyLimit = safeLimit)
            )
        }
    }

    override fun unlockBonusAllowance(amount: Int) {
        // The daily Reel cap is a hard limit; completing a challenge never raises it.
        updateState { current ->
            current.copy(
                bonusAllowance = 0,
                bonusClaimedToday = true,
                challengeCompleted = true,
                challengeFeedback = "Challenge completed. Your daily Reel limit remains unchanged."
            )
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
