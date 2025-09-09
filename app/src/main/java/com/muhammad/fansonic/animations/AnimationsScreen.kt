package com.muhammad.fansonic.animations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R
import com.muhammad.fansonic.animations.components.animations.ArcRotationAnimation
import com.muhammad.fansonic.animations.components.animations.BounceAnimation
import com.muhammad.fansonic.animations.components.animations.CircleScaleAnimation
import com.muhammad.fansonic.animations.components.animations.ClockAnimation
import com.muhammad.fansonic.animations.components.animations.HeartAnimation
import com.muhammad.fansonic.animations.components.animations.PacmanAnimation
import com.muhammad.fansonic.animations.components.animations.ProgressDotAnimation
import com.muhammad.fansonic.animations.components.animations.RotateDotAnimation
import com.muhammad.fansonic.animations.components.animations.RotateTwoDotsAnimation
import com.muhammad.fansonic.animations.components.animations.RotationCircle
import com.muhammad.fansonic.animations.components.animations.RotationSquare
import com.muhammad.fansonic.animations.components.animations.SquareLoadingAnimation
import com.muhammad.fansonic.animations.components.animations.StepFlipAnimation
import com.muhammad.fansonic.animations.components.animations.TwinCircleAnimation
import com.muhammad.fansonic.animations.components.animations.WaveEffect
import com.muhammad.fansonic.animations.components.ui.AnimationCard

@Composable
fun AnimationsScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item(key = "ClockAnimation") {
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Clock Animation") {
                    ClockAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item(key = "WaveEffect") {
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Wave Effect") {
                    WaveEffect(modifier = Modifier.size(150.dp), content = {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(70.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                            ),
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_heart),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.background,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    })
                }
            }
            item(key = "StepFlipAnimation") {
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Step Flip Animation") {
                    StepFlipAnimation(modifier = Modifier.size(150.dp, 80.dp))
                }
            }
            item("BounceAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Bounce Loading") {
                    BounceAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("ProgressDotAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Dots Loading") {
                    ProgressDotAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("HeartAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Heart Animation") {
                    HeartAnimation(modifier = Modifier.size(150.dp), heartSize = 100.dp, lineWidth = 30.dp)
                }
            }
            item("ArcRotationAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Arc Rotation Animation") {
                    ArcRotationAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("RotateDotAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Rotate Dot Animation") {
                    RotateDotAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("SquareLoadingAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Square Loading Animation") {
                    SquareLoadingAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("PacmanAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Pacman Animation") {
                    PacmanAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("RotateTwoDotsAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Rotate Two Dots Animation") {
                    RotateTwoDotsAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("TwinCircleAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Twin Circle Animation") {
                    TwinCircleAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("CircleScaleAnimation"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Circle Scale Animation") {
                    CircleScaleAnimation(modifier = Modifier.size(150.dp))
                }
            }
            item("RotationCircle"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Rotation Circle") {
                    RotationCircle(modifier = Modifier.size(150.dp))
                }
            }
            item("RotationSquare"){
                AnimationCard(modifier = Modifier.fillMaxWidth(), title = "Rotation Square") {
                    RotationSquare(modifier = Modifier.size(150.dp))
                }
            }
        }
    }
}