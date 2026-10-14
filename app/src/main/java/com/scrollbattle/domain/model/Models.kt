package com.scrollbattle.domain.model

enum class TrackingStatus(val label: String) {
    READY("Ready to track"),
    PERMISSION_MISSING("Setup incomplete"),
    PARTIAL_TRACKING("Tracking limited"),
    TESTING("Testing detection"),
    UNAVAILABLE("Service unavailable")
}

data class ScrollSession(
    val id: String,
    val timeLabel: String,
    val durationMinutes: Int,
    val reelCount: Int,
    val periodTag: String,
    val paceTag: String
)

data class DailyAnalytics(
    val dayName: String,
    val shortDay: String,
    val reelsCount: Int,
    val allowance: Int,
    val durationMinutes: Int,
    val sessionCount: Int,
    val status: String,
    val isToday: Boolean = false
)

data class MathQuestion(
    val id: Int,
    val prompt: String,
    val options: List<String>,
    val correctIndex: Int
)

data class AppSettings(
    val dailyLimit: Int = 50,
    val warningThresholdPercent: Int = 80,
    val hardIntervention: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val hapticFeedback: Boolean = true
)

data class ScrollBattleState(
    val todayReels: Int = 0,
    val baseAllowance: Int = 50,
    val bonusAllowance: Int = 0,
    val todayDurationMinutes: Int = 0,
    val todaySessionsCount: Int = 0,
    val trackingStatus: TrackingStatus = TrackingStatus.READY,
    val usageAccessGranted: Boolean = false,
    val accessibilityGranted: Boolean = false,
    val overlayPermissionGranted: Boolean = false,
    val batteryOptimizationIgnored: Boolean = true,
    val settings: AppSettings = AppSettings(),
    val activeChallengeQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val challengeFeedback: String? = null,
    val challengeMistakes: Int = 0,
    val challengeCompleted: Boolean = false,
    val bonusClaimedToday: Boolean = false,
    val selectedAnalyticsDayIndex: Int = 6, // today
    val analyticsTimeSpan: String = "Day", // "Day" or "Week"
    val recentSessions: List<ScrollSession> = emptyList(),
    val weeklyDays: List<DailyAnalytics> = listOf(
        DailyAnalytics("Sunday", "Sun", 0, 50, 0, 0, "No Activity"),
        DailyAnalytics("Monday", "Mon", 0, 50, 0, 0, "No Activity"),
        DailyAnalytics("Tuesday", "Tue", 0, 50, 0, 0, "No Activity"),
        DailyAnalytics("Wednesday", "Wed", 0, 50, 0, 0, "No Activity"),
        DailyAnalytics("Thursday", "Thu", 0, 50, 0, 0, "No Activity"),
        DailyAnalytics("Friday", "Fri", 0, 50, 0, 0, "No Activity"),
        DailyAnalytics("Today", "Today", 0, 50, 0, 0, "Within Limit", isToday = true)
    ),
    val questions: List<MathQuestion> = listOf(
        MathQuestion(1, "Calculate: (14 * 3) + 7", listOf("45", "49", "52", "39"), 1),
        MathQuestion(2, "Calculate: 96 / 8 - 4", listOf("6", "8", "12", "10"), 1),
        MathQuestion(3, "Calculate: (25 * 4) - 37", listOf("57", "63", "65", "73"), 1),
        MathQuestion(4, "Calculate: 15 * 6 - 28", listOf("62", "64", "58", "70"), 0),
        MathQuestion(5, "Calculate: (88 / 4) + 19", listOf("39", "41", "43", "37"), 1),
        MathQuestion(6, "Calculate: 12 * 7 - 35", listOf("47", "49", "51", "53"), 1)
    )
) {
    val totalAllowance: Int get() = baseAllowance + bonusAllowance
    val remainingReels: Int get() = (totalAllowance - todayReels).coerceAtLeast(0)
    val percentageUsed: Int get() = if (totalAllowance > 0) ((todayReels.toFloat() / totalAllowance) * 100).toInt().coerceAtMost(100) else 0
    val isLimitExceeded: Boolean get() = todayReels >= totalAllowance
}
