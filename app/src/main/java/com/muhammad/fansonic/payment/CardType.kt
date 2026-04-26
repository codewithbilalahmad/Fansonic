package com.muhammad.fansonic.payment

import androidx.compose.runtime.*
import com.muhammad.fansonic.R

@Immutable
enum class CardType(
    val title: String,
    val icon: Int,
) {
    NONE("", R.drawable.ic_visa_logo),
    VISA("Visa", R.drawable.ic_visa_logo)
}