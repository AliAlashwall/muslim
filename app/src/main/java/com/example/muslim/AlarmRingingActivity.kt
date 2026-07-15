package com.example.muslim

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.muslim.data.service.AlarmService
import com.example.muslim.presentation.designSystem.theme.Theme
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class AlarmRingingActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val label = intent.getStringExtra("ALARM_LABEL") ?: "Alarm"

        setContent {
            MaterialTheme {
                AlarmRingingScreen(
                    label = label,
                    onDismiss = {
                        stopService(Intent(this, AlarmService::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmRingingScreen(label: String, onDismiss: () -> Unit) {
    val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
    Box(Modifier.fillMaxSize()) {
        Image(
            painterResource(R.drawable.img_kaaba),
            contentDescription = stringResource(R.string.kaaba_image),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(160.dp))
            PrayerHeader(
                label = label,
                currentTime = currentTime
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 26.dp, end = 26.dp, bottom = 90.dp),
                colors = ButtonDefaults.buttonColors(Theme.colors.primary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.dismiss),
                    style = Theme.textStyle.title.medium,
                    color = Theme.colors.onPrimary
                )
            }
        }

    }
}

@Composable
fun PrayerHeader(
    modifier: Modifier = Modifier,
    label: String,
    currentTime: String
) {
    Box(
        modifier = modifier
            .size(300.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.15f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .size(250.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.height(20.dp))

                Text(
                    text = currentTime,
                    style = Theme.textStyle.headline.large,
                    color = Theme.colors.surface
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = label,
                    style = Theme.textStyle.headline.large,
                    color = Theme.colors.surface
                )

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun AlarmRingingScreenPreview() {
    MaterialTheme {
        AlarmRingingScreen("Isha") {}
    }
}
