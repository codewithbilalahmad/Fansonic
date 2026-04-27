package com.muhammad.fansonic.instagram_story.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.muhammad.fansonic.instagram_story.utils.rippleClickable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessageTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    enabled: Boolean = true,
    showTrailingIcon: Boolean = false,
    onClick: () -> Unit = {},
    trailingIcon: Int? = null,
    onTrailingIconClick: () -> Unit = {},
    readOnly: Boolean = false,
    onKeyboardAction: () -> Unit = {},
    contentPadding: PaddingValues = if (trailingIcon != null) PaddingValues(
        start = 16.dp, end = 6.dp, top = 6.dp, bottom = 6.dp
    ) else PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    @StringRes hint: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val trailingScale by animateFloatAsState(
        targetValue = if (showTrailingIcon) 1f else 0f,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "trailingScale"
    )
    BasicTextField(
        state = state,
        enabled = enabled,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        lineLimits = TextFieldLineLimits.SingleLine,
        readOnly = readOnly,
        onKeyboardAction = {
            onKeyboardAction()
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.surface),
        modifier = modifier
            .clip(CircleShape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                shape = CircleShape
            )
            .padding(contentPadding), decorator = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .rippleClickable(onClick = onClick),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(Modifier.weight(1f)) {
                    if (state.text.isEmpty() && hint != null) {
                        Text(
                            text = stringResource(hint),
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        )
                    }
                    innerTextField()
                }
                trailingIcon?.let { icon ->
                    Box(
                        modifier = Modifier
                            .graphicsLayer{
                                scaleX = trailingScale
                                scaleY = trailingScale
                            }
                            .size(height = 40.dp, width = 50.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.onBackground
                            ).clickable(onClick = onTrailingIconClick), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(icon),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
        }
    )
}