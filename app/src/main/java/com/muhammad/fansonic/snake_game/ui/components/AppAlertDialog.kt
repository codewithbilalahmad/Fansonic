package com.muhammad.fansonic.snake_game.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AppAlertDialog(
    onDismiss: () -> Unit,
    title: String? = null,
    message: String? = null,
    confirmText: String? = null,
    titleContent: @Composable () -> Unit = {},messageContent : @Composable () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    confirmButtonColor: Color = MaterialTheme.colorScheme.primary,
    dismissButtonColor: Color = MaterialTheme.colorScheme.error,
    optionButtonColor: Color = MaterialTheme.colorScheme.onBackground,
    cancelText: String? = null,
    optionText: String? = null,
    onOptionClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
    ) {
        Card(
            modifier = Modifier
                .width(260.dp)
                .wrapContentHeight(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center, fontFamily = FontFamily.Monospace
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                } else {
                    titleContent()
                }
                if (message != null) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            lineHeight = 20.sp, color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center, fontFamily = FontFamily.Monospace
                        )
                    )
                } else{
                    Spacer(Modifier.height(10.dp))
                    messageContent()
                }
                Spacer(Modifier.height(24.dp))
                if (confirmText != null) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
                    TextButton(
                        onClick = onConfirmClick, colors = ButtonDefaults.textButtonColors(
                            contentColor = confirmButtonColor
                        ),
                        shape = RoundedCornerShape(0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 45.dp)
                    ) {
                        Text(
                            text = confirmText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold, fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
                if (cancelText != null) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
                    TextButton(
                        onClick = onCancelClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = dismissButtonColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 45.dp),
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Text(
                            text = cancelText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
                if (optionText != null) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 0.5.dp)
                    TextButton(
                        onClick = onOptionClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = optionButtonColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 45.dp),
                        shape = RoundedCornerShape(0.dp),
                    ) {
                        Text(
                            text = optionText,  fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge, fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}