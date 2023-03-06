package com.gram.client.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gram.client.R
import com.gram.client.ui.theme.PrimaryColor

@Composable
fun CustomCheckBox(
    isChecked: Boolean,
    onChecked: () -> Unit = {},
    size: Dp = 30.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(if (isChecked) PrimaryColor else Transparent,)
            .clickable { onChecked() }.border(width = 1.dp, shape = CircleShape, color = if (isChecked) Transparent else Color(
                0xFF9C9C9C
            )
            ),
    ) {
        Icon(
            modifier = Modifier
                .size(size).padding(3.dp)
                .clip(CircleShape),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_checkbox),
            contentDescription = stringResource(R.string.icon_check),
            tint = if (isChecked) Color.White else Transparent,
        )

    }
}
