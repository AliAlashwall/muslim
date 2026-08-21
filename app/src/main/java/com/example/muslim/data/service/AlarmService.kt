package com.example.muslim.data.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.muslim.AlarmRingingActivity
import com.example.muslim.R
import com.example.muslim.data.receiver.AlarmActionReceiver

/**
 * AlarmService is a Foreground Service that handles the actual alarm execution:
 * playing sound, vibrating, and showing a high-priority notification.
 */
class AlarmService : Service() {

    private var ringtone: Ringtone? = null
    private var vibrator: Vibrator? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Check for notification permissions (required for Android 13+)
        val granted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        Log.d("AlarmDebug", "POST_NOTIFICATIONS granted = $granted")

        val alarmId = intent?.getIntExtra("ALARM_ID", -1) ?: -1
        val label = intent?.getStringExtra("ALARM_LABEL") ?: "Alarm"

        // Start as a Foreground Service to ensure it isn't killed by the system
        startForeground(
            NOTIFICATION_ID,
            buildNotification(alarmId, label)
        )
        
        // Start playing the alarm sound (Azan)
        playSound()
        
        // Start the vibration pattern
        vibrate()
        
        // START_STICKY ensures the system attempts to recreate the service if it's killed
        return START_STICKY
    }

    /**
     * Loads and plays the alarm sound resource.
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private fun playSound() {
        // Use a custom audio resource (Azan) for the alarm sound
        val uri = "android.resource://$packageName/${R.raw.azan_alhosary}".toUri()
        ringtone = RingtoneManager.getRingtone(this, uri).apply {
            audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            isLooping = false
            play()
        }
    }

    /**
     * Configures and starts the vibration pattern.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrate() {
        vibrator = getSystemService(Vibrator::class.java)
        val pattern = longArrayOf(0, 1000, 1000) // Start immediately, 1s on, 1s off
        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0)) // 0 means repeat
    }

    /**
     * Builds the high-priority notification required for Foreground Services.
     */
    @SuppressLint("FullScreenIntentPolicy")
    private fun buildNotification(alarmId: Int, label: String): Notification {
        // Intent to open the full-screen alarm activity
        val fullScreenIntent = Intent(this, AlarmRingingActivity::class.java).apply {
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", label)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, alarmId, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent for the 'Dismiss' action button
        val dismissIntent = Intent(this, AlarmActionReceiver::class.java).apply {
            action = AlarmActionReceiver.ACTION_DISMISS
            putExtra("ALARM_ID", alarmId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            this, alarmId * 10 + 1, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent for the 'Snooze' action button
        val snoozeIntent = Intent(this, AlarmActionReceiver::class.java).apply {
            action = AlarmActionReceiver.ACTION_SNOOZE
            putExtra("ALARM_ID", alarmId)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            this, alarmId * 10 + 2, snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mosque)
            .setContentTitle(label)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true) // Show activity even when locked
            .setContentIntent(fullScreenPendingIntent)
            .addAction(0, "Dismiss", dismissPendingIntent)
            .addAction(0, "Snooze", snoozePendingIntent)
            .setOngoing(true) // Cannot be swiped away
            .build()
    }

    override fun onDestroy() {
        // Stop sound and vibration when the service is stopped (Dismissed/Snoozed)
        ringtone?.stop()
        vibrator?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val NOTIFICATION_ID = 1002
        const val CHANNEL_ID = "alarm_channel_two"
    }
}
