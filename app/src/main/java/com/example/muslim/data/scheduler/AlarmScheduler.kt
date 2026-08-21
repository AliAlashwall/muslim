package com.example.muslim.data.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.muslim.MainActivity
import com.example.muslim.data.local.database.entity.AlarmEntity
import com.example.muslim.data.receiver.AlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject

/**
 * AlarmScheduler handles the scheduling and cancellation of alarms using the system AlarmManager.
 */
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Obtain the system AlarmManager service
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    /**
     * Checks if the app has permission to schedule exact alarms (required for Android 12+).
     */
    fun canScheduleExactAlarms(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    /**
     * Schedules a new alarm in the system.
     */
    fun schedule(alarm: AlarmEntity) {
        // Create an intent pointing to AlarmReceiver
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
            putExtra("ALARM_LABEL", alarm.label)
        }

        // Create a PendingIntent that the system will use to trigger the AlarmReceiver
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate the next trigger time in milliseconds
        val triggerTime = nextTriggerMillis(alarm.hour, alarm.minute)

        // Define what happens when the user clicks the alarm icon in the status bar
        val showIntent = PendingIntent.getActivity(
            context, alarm.id,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Use setAlarmClock to ensure high priority and bypass power-saving modes (Doze)
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(triggerTime, showIntent),
            pendingIntent
        )
    }

    /**
     * Cancels a previously scheduled alarm.
     */
    fun cancel(alarm: AlarmEntity) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, alarm.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Cancel the alarm in the system AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Calculates the time in milliseconds for the next occurrence of the alarm.
     */
    private fun nextTriggerMillis(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // If the time has already passed today, schedule it for tomorrow
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        return next.timeInMillis
    }
}