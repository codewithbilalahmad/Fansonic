package com.muhammad.fansonic.native_ads

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

@Composable
fun NativeAds(modifier: Modifier = Modifier, type: NativeAdType, isInternetConnected : Boolean) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var hasAdsLoaded by remember { mutableStateOf(false) }
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    DisposableEffect(isInternetConnected) {
        if(isInternetConnected && !hasAdsLoaded){
            isLoading = true
            val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110").forNativeAd { ad ->
                nativeAd = ad
                isLoading = false
                hasAdsLoaded = true
            }.withAdListener(object  : AdListener(){
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoading = false
                    hasAdsLoaded = false
                    nativeAd = null
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            isLoading = false
        }
        onDispose {  }
    }
    if(!hasAdsLoaded && !isLoading) return
    AnimatedContent(
        targetState = isLoading,
        modifier = modifier,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { loading ->
        if (loading) {
            when(type){
                NativeAdType.BANNER -> NativeBannerLoading(modifier = modifier)
                NativeAdType.BIG_BANNER -> NativeBigBannerLoading(modifier = modifier)
                NativeAdType.NEW_NATIVE_WITH_MEDIA -> NativeWithMediaLoading(modifier = modifier)
            }
        } else{
            when(type){
                NativeAdType.BANNER -> NativeBannerAd(modifier = modifier, nativeAd = nativeAd)
                NativeAdType.BIG_BANNER -> NativeBigBannerAd(modifier = modifier, nativeAd = nativeAd)
                NativeAdType.NEW_NATIVE_WITH_MEDIA -> NativeWithMediaAd(modifier = modifier, nativeAd = nativeAd)
            }
        }
    }
}