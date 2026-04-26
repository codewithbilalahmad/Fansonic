package com.muhammad.fansonic.payment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.R

@Composable
fun PaymentScreen() {
    val layoutDirection = LocalLayoutDirection.current
    val username = rememberTextFieldState()
    val cardNumber = rememberTextFieldState()
    val expiryNumber = rememberTextFieldState()
    val cvcNumber = rememberTextFieldState()
    val cardNumberTransformation = InputTransformation {
        val digits = asCharSequence().filter { it.isDigit() }.take(16).chunked(4).joinToString(" ")
        replace(0, length, digits)
    }
    val expiryTransformation = InputTransformation {
        val digits = asCharSequence().filter { it.isDigit() }.take(4)
        val formatted = buildString {
            digits.forEachIndexed { index, c ->
                append(c)
                if (index == 1 && digits.length > 2) append(" / ")
            }
        }
        replace(0, length, formatted)
    }
    val cvcTransformation = InputTransformation {
        val digits = asCharSequence().filter { it.isDigit() }.take(3)
        replace(0, length, digits)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
        , topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.add_payment))
                },
                actions = {
                    IconButton(
                        onClick = {},
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        shapes = IconButtonDefaults.shapes()
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_credit_card),
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }, bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PrimaryButton(
                    text = stringResource(R.string.continue_to),
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    enabled = username.text.isNotEmpty() && cardNumber.text.length >= 16 && expiryNumber.text.length >= 4 && cvcNumber.text.length >= 3
                )
            }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(
                start = paddingValues.calculateLeftPadding(layoutDirection) + 16.dp,
                end = paddingValues.calculateEndPadding(layoutDirection) + 16.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 24.dp
            )
        ) {
            item("PaymentCard") {
                PaymentCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .padding(top = 16.dp, bottom = 32.dp),
                    username = username,
                    cardNumber = cardNumber,
                    expiryNumber = expiryNumber,
                    cvcNumber = cvcNumber
                )
            }
            item("username") {
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    state = username,
                    hint = R.string.card_holder
                )
            }
            item("cardNumber") {
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .animateItem(),
                    inputTransformation = cardNumberTransformation,
                    state = cardNumber, keyboardType = KeyboardType.Number,
                    hint = R.string.card_number
                )
            }
            item("expiry_and_cvc") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .animateItem(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppTextField(
                        modifier = Modifier.weight(1f),
                        inputTransformation = expiryTransformation,
                        state = expiryNumber, keyboardType = KeyboardType.Number,
                        hint = R.string.expiry
                    )
                    AppTextField(
                        modifier = Modifier.weight(1f),
                        inputTransformation = cvcTransformation,
                        state = cvcNumber, keyboardType = KeyboardType.Number,
                        hint = R.string.cvc
                    )
                }
            }
        }
    }
}