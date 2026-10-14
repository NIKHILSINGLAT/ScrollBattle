package com.scrollbattle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.scrollbattle.ui.ScrollBattleViewModel
import com.scrollbattle.ui.screens.*

@Composable
fun MainNavigation(
    pendingDestination: String? = null,
    onClearPendingDestination: () -> Unit = {},
    viewModel: ScrollBattleViewModel = viewModel(
        factory = ScrollBattleViewModel.provideFactory(androidx.compose.ui.platform.LocalContext.current)
    )
) {
    val state by viewModel.uiState.collectAsState()
    val initialKey = if (!viewModel.isOnboardingCompleted() && !state.accessibilityGranted) WelcomeKey else HomeKey
    val backStack = rememberNavBackStack(initialKey)

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { dest ->
            if (dest == "maths_challenge") {
                if (backStack.lastOrNull() != MathsChallengeKey) {
                    backStack.add(MathsChallengeKey)
                }
            } else if (dest == "limit_reached") {
                if (backStack.lastOrNull() != LimitReachedKey) {
                    backStack.add(LimitReachedKey)
                }
            }
        }
    }

    androidx.compose.runtime.LaunchedEffect(pendingDestination) {
        if (pendingDestination == "maths_challenge") {
            if (backStack.lastOrNull() != MathsChallengeKey) {
                backStack.add(MathsChallengeKey)
            }
            onClearPendingDestination()
        } else if (pendingDestination == "limit_reached") {
            if (backStack.lastOrNull() != LimitReachedKey) {
                backStack.add(LimitReachedKey)
            }
            onClearPendingDestination()
        }
    }

    fun navigateTo(key: androidx.navigation3.runtime.NavKey) {
        if (backStack.lastOrNull() != key) {
            backStack.add(key)
        }
    }

    fun switchTab(key: androidx.navigation3.runtime.NavKey) {
        if (backStack.lastOrNull() != key) {
            while (backStack.isNotEmpty()) {
                backStack.removeLastOrNull()
            }
            backStack.add(HomeKey)
            if (key !is HomeKey) {
                backStack.add(key)
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            // 1. You / Home Dashboard
            entry<HomeKey> {
                HomeScreen(
                    state = state,
                    onNavigateToBattle = { switchTab(BattleKey) },
                    onNavigateToProgress = { switchTab(ProgressKey) },
                    onNavigateToDetails = { navigateTo(DailyDetailsKey) },
                    onNavigateToSettings = { navigateTo(SettingsKey) },
                    onNavigateToReadiness = { navigateTo(TrackingStatusKey) },
                    onNavigateToPermissions = { navigateTo(PermissionsSetupKey) }
                )
            }

            // 2. Battle / Daily Limit
            entry<BattleKey> {
                BattleScreen(
                    state = state,
                    onNavigateToHome = { switchTab(HomeKey) },
                    onNavigateToProgress = { switchTab(ProgressKey) },
                    onStartChallenge = { navigateTo(MathsChallengeKey) },
                    onNavigateToLimitReached = { navigateTo(LimitReachedKey) },
                    onSetDailyLimit = { viewModel.setDailyLimit(it) },
                    onSimulateReel = { viewModel.simulateReelScroll() }
                )
            }

            // 3. Maths Challenge
            entry<MathsChallengeKey> {
                MathsChallengeScreen(
                    state = state,
                    onSelectOption = { viewModel.selectChallengeOption(it) },
                    onSubmitAnswer = { viewModel.submitChallengeAnswer() },
                    onResetChallenge = { viewModel.resetChallenge() },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            // 4. Progress / Analytics
            entry<ProgressKey> {
                ProgressScreen(
                    state = state,
                    onNavigateToHome = { switchTab(HomeKey) },
                    onNavigateToBattle = { switchTab(BattleKey) },
                    onSelectDay = { viewModel.selectAnalyticsDay(it) },
                    onSetTimeSpan = { viewModel.setAnalyticsTimeSpan(it) },
                    onNavigateToDetails = { navigateTo(DailyDetailsKey) }
                )
            }

            // 5. Welcome / Onboarding
            entry<WelcomeKey> {
                WelcomeScreen(
                    onGetStarted = { navigateTo(PermissionsSetupKey) },
                    onLearnMore = { navigateTo(PrivacyDataKey) }
                )
            }

            // 6. Permissions Setup
            entry<PermissionsSetupKey> {
                PermissionsSetupScreen(
                    state = state,
                    onContinue = {
                        viewModel.completeOnboarding()
                        switchTab(HomeKey)
                    },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            // 7. Tracking Status / Readiness
            entry<TrackingStatusKey> {
                val currentContext = androidx.compose.ui.platform.LocalContext.current
                TrackingStatusScreen(
                    state = state,
                    onSetTrackingStatus = { viewModel.setTrackingStatus(it) },
                    onTestDetection = {
                        viewModel.incrementScrollCount(1)
                        currentContext.sendBroadcast(android.content.Intent("com.scrollbattle.ACTION_SHOW_PILL"))
                    },
                    onGoToDashboard = {
                        viewModel.completeOnboarding()
                        switchTab(HomeKey)
                    },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            // 8. Limit Reached
            entry<LimitReachedKey> {
                LimitReachedScreen(
                    state = state,
                    onStartChallenge = { navigateTo(MathsChallengeKey) },
                    onDoneForToday = { switchTab(HomeKey) },
                    onClose = { backStack.removeLastOrNull() }
                )
            }

            // 9. Settings
            entry<SettingsKey> {
                SettingsScreen(
                    state = state,
                    onSetDailyLimit = { viewModel.setDailyLimit(it) },
                    onSetWarningThreshold = { viewModel.setWarningThreshold(it) },
                    onToggleIntervention = { viewModel.toggleHardIntervention(it) },
                    onNavigateToTrackingStatus = { navigateTo(TrackingStatusKey) },
                    onNavigateToTroubleshooting = { navigateTo(TroubleshootingKey) },
                    onNavigateToPrivacy = { navigateTo(PrivacyDataKey) },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            // 10. Daily Details
            entry<DailyDetailsKey> {
                DailyDetailsScreen(
                    state = state,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            // 11. Troubleshooting
            entry<TroubleshootingKey> {
                val currentContext = androidx.compose.ui.platform.LocalContext.current
                TroubleshootingScreen(
                    state = state,
                    onTestDetection = {
                        viewModel.incrementScrollCount(1)
                        currentContext.sendBroadcast(android.content.Intent("com.scrollbattle.ACTION_SHOW_PILL"))
                    },
                    onNavigateToPermissions = { navigateTo(PermissionsSetupKey) },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }

            // 12. Privacy & Data
            entry<PrivacyDataKey> {
                PrivacyDataScreen(
                    state = state,
                    onDeleteHistory = { viewModel.deleteHistory() },
                    onResetSettings = { viewModel.resetSettings() },
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
