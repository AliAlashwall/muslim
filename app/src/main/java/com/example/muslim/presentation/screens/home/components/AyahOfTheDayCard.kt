package com.example.muslim.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muslim.R
import com.example.muslim.presentation.designSystem.theme.Theme
import com.example.muslim.presentation.screens.home.AyahOfDay

@Composable
fun AyahOfTheDayCard(ayah: AyahOfDay, modifier: Modifier = Modifier) {
    Surface(
        color = Theme.colors.tertiaryContainer,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Theme.colors.tertiary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Theme.colors.surface,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.aya_of_the_day),
                color = Theme.colors.onTertiary,
                style = Theme.textStyle.label.small
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = ayah.text,
                color = Theme.colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(
                color = Color(0xFFC9A227).copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth(0.2f),
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = ayah.reference,
                color = Theme.colors.onTertiary,
                style = Theme.textStyle.body.medium
            )
        }
    }
}
