package com.example.muslim.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.muslim.presentation.designSystem.theme.Theme

@Composable
fun SettingsIconButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Theme.colors.onPrimary.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(34.dp)) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "الإعدادات",
                tint = Theme.colors.onPrimary,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}