package com.example.muslim

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.muslim.presentation.designSystem.theme.MuslimTheme
import com.example.muslim.presentation.screens.prayerTimes.PrayerTimesScreen
import com.example.muslim.presentation.screens.prayerTimes.sampleHeaderInfo
import com.example.muslim.presentation.screens.prayerTimes.samplePrayerTimes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuslimTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
/*                    val samplePrayerTimes = PrayerTimesResponse(
                        date = "10 يوليو 2026",
                        city = "القاهرة",
                        country = "مصر",
                        prayerTimes = listOf(
                            PrayerTime("Fajr", "04:45", "الفجر"),
                            PrayerTime("Sunrise", "06:20", "الشروق"),
                            PrayerTime("Dhuhr", "12:58", "الظهر"),
                            PrayerTime("Asr", "16:45", "العصر"),
                            PrayerTime("Maghrib", "19:36", "المغرب"),
                            PrayerTime("Isha", "21:06", "العشاء")
                        )
                    )*/
                    PrayerTimesScreen(
                        header = sampleHeaderInfo(),
                        prayers = samplePrayerTimes()
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MuslimTheme {
        Greeting("Android")
    }
}