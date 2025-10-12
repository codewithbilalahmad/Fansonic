package com.muhammad.fansonic.step_progress

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressScreen() {
    var currentStep by remember { mutableIntStateOf(1) }
    val totalSteps = 5
    val stepContent = remember {
        listOf(
            "Welcome to your journey! Let's get started with the onboarding process.",
            "Perfect! Now let's set up your preferences and configure the basic settings.",
            "Great progress! Time to review your configuration and make any adjustments.",
            "Almost there! Please review all your settings before we finalize everything.",
            "Congratulations! You have successfully completed the entire setup process."
        )
    }
    val stepTitles = remember {
        listOf(
            "Getting Started",
            "Configuration",
            "Customization",
            "Final Review",
            "All Done!"
        )
    }
    Surface(modifier = Modifier.fillMaxSize().systemBarsPadding(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.1f),
                                Color(0xFF2196F3).copy(alpha = 0.1f)
                            )
                        ), shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp), contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Setup Progress",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Step $currentStep of $totalSteps",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            ProgressTick(
                totalSteps = totalSteps,
                currentStep = currentStep,
                stepLabel = listOf("Start", "Setup", "Configure", "Review", "Complete"),
                onStepClick = { step ->
                    currentStep = step
                })
            Spacer(Modifier.height(40.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
                        spotColor = Color(0xFF4CAF50).copy(0.1f)
                    ), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Crossfade(
                    targetState = currentStep,
                    animationSpec = tween(durationMillis = 500),
                    label = "step_transition"
                ) { step ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stepTitles.getOrElse(step - 1) { "Step $step" },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = stepContent.getOrElse(step - 1) { "" },
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                            color = MaterialTheme.colorScheme.onSurface.copy(0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if (currentStep < totalSteps) {
                        currentStep++
                    } else currentStep = 1
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color(0xFF4CAF50).copy(0.3f),
                        spotColor = Color(0xFF4CAF50).copy(0.3f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (currentStep < totalSteps) "Continue to Next Step" else "Start Over",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        ),
                        color = Color.White
                    )

                    if (currentStep < totalSteps) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "→",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }
                }
            }
            Text(
                text = if (currentStep < totalSteps) {
                    "Tap any step above to jump directly to it"
                } else {
                    "You've completed all steps! 🎉"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ProgressTick(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color(0xFF4CAF50),
    inactiveColor: Color = Color(0xFFE0E0E0),
    onStepClick: (Int) -> Unit,
    stepLabel: List<String> = emptyList(),
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(totalSteps) { index ->
                val step = index + 1
                val isActive = step <= currentStep
                val isCurrent = step == currentStep
                val color by animateColorAsState(
                    targetValue = if (isActive) activeColor else inactiveColor,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                val scale by animateFloatAsState(
                    targetValue = if (isCurrent) 1.2f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                val elevation by animateDpAsState(
                    targetValue = if (isCurrent) 8.dp else if (isActive) 4.dp else 0.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .scale(scale)
                        .clip(
                            CircleShape
                        )
                        .shadow(
                            elevation = elevation,
                            shape = CircleShape,
                            ambientColor = if (isActive) activeColor.copy(0.3f) else Color.Transparent,
                            spotColor = if (isActive) activeColor.copy(0.3f) else Color.Transparent
                        )
                        .background(color = color, shape = CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                onStepClick(step)
                            }
                        )
                ) {
                    val textColor by animateColorAsState(
                        targetValue = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface.copy(
                            0.6f
                        ), animationSpec = tween(durationMillis = 300)
                    )
                    Text(
                        text = if (isActive) "✓" else step.toString(),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
                }
                if (step != totalSteps) {
                    val lineColor by animateColorAsState(
                        targetValue = if (step < currentStep) activeColor else inactiveColor,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                            .background(lineColor, CircleShape)
                    )
                }
            }
        }
        if (stepLabel.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(totalSteps) { index ->
                    val step = index + 1
                    val isActive = step <= currentStep
                    val label = stepLabel.getOrNull(index) ?: "Step $step"
                    val labelColor by animateColorAsState(
                        targetValue = if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                            0.5f
                        ), animationSpec = tween(durationMillis = 300)
                    )
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = label,
                            color = labelColor,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
                                fontSize = 11.sp
                            ),
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}