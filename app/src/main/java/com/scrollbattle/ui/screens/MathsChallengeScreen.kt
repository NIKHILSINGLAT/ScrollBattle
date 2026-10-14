package com.scrollbattle.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.scrollbattle.domain.model.ScrollBattleState
import com.scrollbattle.theme.*
import com.scrollbattle.ui.components.*

@Composable
fun MathsChallengeScreen(
    state: ScrollBattleState,
    onSelectOption: (Int) -> Unit,
    onSubmitAnswer: () -> Unit,
    onResetChallenge: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val currentQuestion = state.questions.getOrNull(state.activeChallengeQuestionIndex)

    Scaffold(
        containerColor = CanvasBackground,
        topBar = {
            ScrollBattleTopBar(
                title = "Active Challenge",
                subtitle = "Earn your next 20 Reels",
                canNavigateBack = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Progress indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${state.activeChallengeQuestionIndex + 1} of ${state.questions.size}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    Text(
                        text = "${state.questions.size - state.activeChallengeQuestionIndex} remaining",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                ChallengeProgressIndicator(
                    currentIndex = state.activeChallengeQuestionIndex,
                    totalQuestions = state.questions.size
                )
            }

            if (state.challengeCompleted) {
                // Success State Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(CardSurface)
                            .border(1.dp, RecoveryGreen, RoundedCornerShape(24.dp))
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(RecoveryGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MilitaryTech,
                                    contentDescription = null,
                                    tint = RecoveryGreen,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Text(
                                text = "Challenge Complete!",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "You solved all 6 calculations with cognitive focus. An additional +20 Reels have been added to your daily allowance.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            PrimaryActionButton(
                                text = "Return to Battle",
                                onClick = onNavigateBack
                            )
                        }
                    }
                }
            } else if (currentQuestion != null) {
                // Question Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(CardSurface)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "CALCULATION ${state.activeChallengeQuestionIndex + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryOrange
                                )
                                if (state.challengeMistakes > 0) {
                                    Text(
                                        text = "${state.challengeMistakes} mistakes",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CriticalRed
                                    )
                                }
                            }
                            Text(
                                text = currentQuestion.prompt,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }

                // Options list
                items(currentQuestion.options.size) { index ->
                    val optionText = currentQuestion.options[index]
                    val letter = listOf("A", "B", "C", "D").getOrElse(index) { "?" }
                    val isSelected = state.selectedOptionIndex == index

                    QuizAnswerOption(
                        letter = letter,
                        text = optionText,
                        isSelected = isSelected,
                        onClick = { onSelectOption(index) }
                    )
                }

                // Feedback Banner
                if (state.challengeFeedback != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceElevated)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = state.challengeFeedback ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (state.challengeFeedback?.contains("Incorrect") == true) CriticalRed else RecoveryGreen
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    PrimaryActionButton(
                        text = "Submit answer",
                        trailingIcon = Icons.Default.ArrowForward,
                        enabled = state.selectedOptionIndex != null,
                        onClick = onSubmitAnswer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SecondaryActionButton(
                        text = "Exit Challenge",
                        onClick = onNavigateBack
                    )
                }
            }
        }
    }
}
