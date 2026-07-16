package com.example.muslim.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.screens.home.HomeFeature
import com.example.muslim.presentation.screens.home.HomeFeatureAccent

@Composable
fun FeatureGrid(
    features: List<HomeFeature>,
    onFeatureClick: (HomeFeature) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        features.chunked(2).forEach { rowFeatures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowFeatures.forEach { feature ->
                    FeatureCard(
                        feature = feature,
                        modifier = Modifier.weight(1f),
                        onClick = { onFeatureClick(feature) }
                    )
                }
                if (rowFeatures.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun FeatureIcon(
    modifier: Modifier = Modifier,
    feature: HomeFeature,
) {
    val iconBg = when (feature.accent) {
        HomeFeatureAccent.GOLD -> Theme.colors.secondary
        HomeFeatureAccent.GREEN -> Theme.colors.primary
    }
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(iconBg),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = feature.icon,
            contentDescription = null,
            tint = Theme.colors.surface,
            modifier = Modifier.size(28.dp)
        )
    }
}