package com.muhammad.fansonic.foreground_service.util

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.muhammad.fansonic.R
import com.muhammad.fansonic.foreground_service.ForegroundApplication
import com.muhammad.fansonic.foreground_service.service.ServiceHelper
import com.muhammad.fansonic.foreground_service.util.Constants.NOTIFICATION_CHANNEL_ID

fun formatTime(hours: String, minutes: String, seconds: String): String {
    return "$hours:$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}

fun provideNotificationBuilder(): NotificationCompat.Builder {
    val context = ForegroundApplication.INSTANCE
    return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Stopwatch")
        .setContentText("00:00:00")
        .setOngoing(true)
        .setContentIntent(ServiceHelper.clickPendingIntent())
        .setSilent(true)
        .addAction(0, "Stop", ServiceHelper.stopPendingIntent())
        .addAction(0, "Cancel", ServiceHelper.cancelPendingIntent())
}

fun provideNotificationManager(): NotificationManager {
    val context = ForegroundApplication.INSTANCE
    return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}