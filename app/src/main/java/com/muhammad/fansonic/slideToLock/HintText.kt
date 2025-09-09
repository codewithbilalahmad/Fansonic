package com.muhammad.fansonic.slideToLock

import androidx.compose.runtime.*

@Immutable
data class HintText(val defaultText : String,val  slidedText : String){
    companion object{
        @Stable
        fun defaultHintText() : HintText{
            return HintText(
                defaultText = "Slide to unlocked",
                slidedText = "Please wait a moment.."
            )
        }
    }
}
