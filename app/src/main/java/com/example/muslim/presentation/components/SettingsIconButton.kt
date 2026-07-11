package com.example.muslim.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.muslim.R
import com.example.muslim.presentation.designSystem.theme.Theme

@Composable
fun SettingsIconButton(onClick: () -> Unit) {

    IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings),
            tint = Theme.colors.onPrimary,
            modifier = Modifier.size(16.dp)
        )
    }
}