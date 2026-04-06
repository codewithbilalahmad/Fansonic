package com.muhammad.fansonic.native_ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.*

@Composable
fun NativeBannerLoading(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(70.5.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp)
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(6.dp))
                .loadingEffect()
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    Modifier
                        .height(16.dp)
                        .width(22.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .loadingEffect()
                )
                Box(
                    Modifier
                        .height(14.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp))
                        .loadingEffect()
                )
            }

            Column(
                modifier = Modifier.padding(top = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    Modifier
                        .height(11.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .loadingEffect()
                )
                Box(
                    Modifier
                        .height(11.dp)
                        .fillMaxWidth(0.75f)
                        .clip(RoundedCornerShape(4.dp))
                        .loadingEffect()
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            Modifier
                .width(80.dp)
                .height(36.dp)
                .clip(CircleShape)
                .loadingEffect()
        )
    }
}

@Composable
fun NativeBigBannerLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp), verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .loadingEffect()
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        Modifier
                            .height(14.dp)
                            .width(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .loadingEffect()
                    )
                    Box(
                        Modifier
                            .height(8.dp)
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(4.dp))
                            .loadingEffect()
                    )
                }

                Box(
                    Modifier
                        .padding(top = 6.dp)
                        .height(13.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .loadingEffect()
                )
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .height(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .loadingEffect()
        )
    }
}

@Composable
fun NativeWithMediaLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(333.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp), verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .loadingEffect()
        )
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .loadingEffect()
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        Modifier
                            .height(14.dp)
                            .width(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .loadingEffect()
                    )
                    Box(
                        Modifier
                            .height(8.dp)
                            .fillMaxWidth(0.8f)
                            .clip(RoundedCornerShape(4.dp))
                            .loadingEffect()
                    )
                }

                Box(
                    Modifier
                        .padding(top = 6.dp)
                        .height(13.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .loadingEffect()
                )
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
                .height(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .loadingEffect()
        )
    }
}
