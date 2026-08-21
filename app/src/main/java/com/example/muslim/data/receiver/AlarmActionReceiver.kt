package com.example.muslim.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.muslim.data.service.AlarmService

/**
 * AlarmActionReceiver handles user interactions with the alarm notification (Dismiss/Snooze).
 */
class AlarmActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        
        // Handle different notification actions
        when (intent.action) {
            ACTION_DISMISS -> {
                // Stop the AlarmService to end the sound and vibration
                context.stopService(Intent(context, AlarmService::class.java))
            }
            ACTION_SNOOZE -> {
                // Currently just stops the service; snooze logic could be added here
                context.stopService(Intent(context, AlarmService::class.java))
            }
        }
    }

    companion object {
        // Unique action strings for the notification buttons
        const val ACTION_DISMISS = "com.yourname.muslim.ACTION_DISMISS"
        const val ACTION_SNOOZE = "com.yourname.muslim.ACTION_SNOOZE"
    }
}