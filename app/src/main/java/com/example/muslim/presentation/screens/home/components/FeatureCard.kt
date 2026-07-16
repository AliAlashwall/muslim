package com.example.muslim.presentation.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.screens.home.HomeFeature


@Composable
fun FeatureCard(
    feature: HomeFeature,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onPrayersClicked: () -> Unit
) {
    Surface(
        color = Theme.colors.surface,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable(
                    onClick = {
                        if (feature.id == 2) {
                            onPrayersClicked()
                        }
                    }
                ),
            horizontalAlignment = Alignment.Start
        ) {
            FeatureIcon(feature = feature)
            Spacer(Modifier.height(12.dp))
            Text(
                text = feature.title,
                color = Theme.colors.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = feature.subtitle,
                color = Theme.colors.onTertiary,
                fontSize = 12.sp
            )
        }
    }
}