package com.example.muslim.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.muslim.data.service.AlarmService

class AlarmActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        when (intent.action) {
            ACTION_DISMISS -> {
                context.stopService(Intent(context, AlarmService::class.java))
            }
            ACTION_SNOOZE -> {
                context.stopService(Intent(context, AlarmService::class.java))
            }
        }
    }

    companion object {
        const val ACTION_DISMISS = "com.yourname.muslim.ACTION_DISMISS"
        const val ACTION_SNOOZE = "com.yourname.muslim.ACTION_SNOOZE"
    }
}