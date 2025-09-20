package com.muhammad.fansonic.foreground_service.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_CANCEL
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_START
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_STOP
import com.muhammad.fansonic.foreground_service.util.Constants.NOTIFICATION_CHANNEL_ID
import com.muhammad.fansonic.foreground_service.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.muhammad.fansonic.foreground_service.util.Constants.NOTIFICATION_ID
import com.muhammad.fansonic.foreground_service.util.Constants.STOPWATCH_STATE
import com.muhammad.fansonic.foreground_service.util.formatTime
import com.muhammad.fansonic.foreground_service.util.pad
import com.muhammad.fansonic.foreground_service.util.provideNotificationBuilder
import com.muhammad.fansonic.foreground_service.util.provideNotificationManager
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class StopWatchService : Service() {
    private val notificationManager: NotificationManager = provideNotificationManager()
    private val notificationBuilder: NotificationCompat.Builder = provideNotificationBuilder()
    private val binder = StopWatchBinder()
    private var duration: Duration = Duration.ZERO
    private lateinit var timer: Timer
    var hours = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var seconds = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(StopWatchState.IDLE)
        private set

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            StopWatchState.STARTED.name -> {
                setStopButton()
                startForegroundService()
                startStopWatch { hours, minutes, seconds ->
                    updateNotification(hours, minutes, seconds)
                    updateTimer()
                }
            }

            StopWatchState.STOPPED.name -> {
                stopStopWatch()
                setResumeButton()
            }

            StopWatchState.CANCELLED.name -> {
                stopStopWatch()
                cancelStopWatch()
                stopForegroundService()
            }
        }
        intent?.action?.let { action ->
            when (action) {
                ACTION_SERVICE_START -> {
                    setStopButton()
                    startForegroundService()
                    startStopWatch { hours, minutes, seconds ->
                        updateNotification(hours, minutes, seconds)
                        updateTimer()
                    }
                }

                ACTION_SERVICE_STOP -> {
                    stopStopWatch()
                    setResumeButton()
                }

                ACTION_SERVICE_CANCEL -> {
                    stopStopWatch()
                    cancelStopWatch()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder = binder
    private fun startStopWatch(onTick: (hour: String, minute: String, seconds: String) -> Unit) {
        currentState.value = StopWatchState.STARTED
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimer()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopStopWatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = StopWatchState.STOPPED
    }

    private fun cancelStopWatch() {
        duration = Duration.ZERO
        currentState.value = StopWatchState.IDLE
        updateTimer()
    }

    private fun updateTimer() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@StopWatchService.hours.value = hours.toInt().pad()
            this@StopWatchService.minutes.value = minutes.pad()
            this@StopWatchService.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            ).build()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setStopButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(0, "Stop", ServiceHelper.stopPendingIntent())
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0, NotificationCompat.Action(0, "Resume", ServiceHelper.resumePendingIntent())
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    fun updateStateFromNotification(state: String) {
        when (state) {
            StopWatchState.STARTED.name -> {
                setStopButton()
                startForegroundService()
                startStopWatch { hours, minutes, seconds ->
                    updateNotification(hours, minutes, seconds)
                    updateTimer()
                }
            }

            StopWatchState.STOPPED.name -> {
                stopStopWatch()
                setResumeButton()
            }

            StopWatchState.CANCELLED.name -> {
                stopStopWatch()
                cancelStopWatch()
                stopForegroundService()
            }
        }
    }

    inner class StopWatchBinder() : Binder() {
        fun getService(): StopWatchService = this@StopWatchService
    }
}