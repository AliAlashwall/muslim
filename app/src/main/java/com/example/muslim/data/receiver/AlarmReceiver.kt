package com.example.muslim.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.muslim.data.service.AlarmService


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val label = intent.getStringExtra("ALARM_LABEL") ?: "Alarm"

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", label)
        }
        // Foreground service start from a BroadcastReceiver is explicitly
        // allowed even in the background — this is the exemption that makes
        // the whole flow work while the app process is dead.
        ContextCompat.startForegroundService(context, serviceIntent)
    }

}