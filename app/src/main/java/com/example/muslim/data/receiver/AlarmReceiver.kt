package com.example.muslim.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.muslim.data.service.AlarmService


/**
 * AlarmReceiver is a BroadcastReceiver that is triggered by the Android system's AlarmManager.
 * It serves as the entry point when an alarm fires.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve alarm details passed through the intent
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val label = intent.getStringExtra("ALARM_LABEL") ?: "Alarm"

        // Prepare the intent to start the AlarmService which will play the sound and show the notification
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", label)
        }

        // Starting a Foreground Service from a BroadcastReceiver is a system-allowed exemption
        // to background execution limits, ensuring the alarm sounds even if the app is killed.
        ContextCompat.startForegroundService(context, serviceIntent)
    }

}