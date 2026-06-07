package com.example.personalwellnessapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.Calendar

class NotificationHelper(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "hydration_reminder_channel"
        private const val NOTIFICATION_ID = 1001
        private const val REQUEST_CODE = 2002
        private const val ALARM_REQUEST_CODE = 3003
        private const val TAG = "NotificationHelper"
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Hydration Reminder"
            val descriptionText = "Reminders to drink water"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created")
        }
    }
    
    fun showHydrationNotification() {
        Log.d(TAG, "Showing hydration notification")
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 
            REQUEST_CODE, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_habit) // You might want to create a water drop icon
            .setContentTitle("Stay Hydrated!")
            .setContentText("Don't forget to drink water!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        try {
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
            Log.d(TAG, "Hydration notification shown successfully")
        } catch (e: SecurityException) {
            Log.e(TAG, "Failed to show notification due to permission error", e)
        }
    }
    
    fun scheduleRepeatingNotification(intervalMinutes: Long) {
        Log.d(TAG, "Scheduling repeating notification with interval: $intervalMinutes minutes")
        cancelScheduledNotifications()
        
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, HydrationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            ALARM_REQUEST_CODE, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            add(Calendar.MINUTE, intervalMinutes.toInt())
        }
        
        // Set repeating alarm
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMinutes * 60 * 1000, // interval in milliseconds
            pendingIntent
        )
        
        Log.d(TAG, "Repeating notification scheduled")
    }
    
    fun cancelScheduledNotifications() {
        Log.d(TAG, "Cancelling scheduled notifications")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, HydrationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            ALARM_REQUEST_CODE, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Scheduled notifications cancelled")
    }
}

// BroadcastReceiver to handle scheduled notifications
class HydrationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("HydrationReceiver", "Received broadcast for hydration reminder")
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showHydrationNotification()
    }
}