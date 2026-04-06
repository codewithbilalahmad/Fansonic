package com.muhammad.fansonic.native_ads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.gms.ads.nativead.NativeAd

@Composable
fun NativeBannerAd(
    modifier: Modifier = Modifier,
    nativeAd: NativeAd?,
) {
    if (nativeAd == null) return
    NativeAdContainer(nativeAd = nativeAd, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            nativeAd.icon?.let { icon ->
                NativeAdIconView(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                ) {
                    AsyncImage(
                        model = icon.uri.toString(),
                        contentDescription = "Ad Icon",
                        modifier = Modifier.size(36.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    NativeAdAttribution()
                    NativeAdHeadlineView(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nativeAd.headline ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                nativeAd.body?.let { body ->
                    NativeAdBodyView(modifier = Modifier.padding(top = 6.dp)) {
                        Text(
                            text = body,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.surface
                            ),
                            maxLines = 2,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            nativeAd.callToAction?.let { ctaText ->
                NativeAdCallToActionView {
                    NativeAdButton(
                        text = ctaText,
                        shape = CircleShape,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        padding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )
                }
            }
        }
    }
}