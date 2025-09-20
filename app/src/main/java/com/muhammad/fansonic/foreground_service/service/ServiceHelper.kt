package com.muhammad.fansonic.foreground_service.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.muhammad.fansonic.MainActivity
import com.muhammad.fansonic.foreground_service.ForegroundApplication
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_CANCEL
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_START
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_STOP
import com.muhammad.fansonic.foreground_service.util.Constants.CANCEL_REQUEST_CODE
import com.muhammad.fansonic.foreground_service.util.Constants.CLICK_REQUEST_CODE
import com.muhammad.fansonic.foreground_service.util.Constants.RESUME_REQUEST_CODE
import com.muhammad.fansonic.foreground_service.util.Constants.STOPWATCH_STATE
import com.muhammad.fansonic.foreground_service.util.Constants.STOP_REQUEST_CODE

object ServiceHelper {
    private val context = ForegroundApplication.INSTANCE

    fun clickPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return PendingIntent.getActivity(
            context,
            CLICK_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun stopPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchState.STOPPED.name)
            action = ACTION_SERVICE_STOP
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(
            context,
            STOP_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun resumePendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchState.STARTED.name)
            action = ACTION_SERVICE_START
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(
            context,
            RESUME_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    fun cancelPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(STOPWATCH_STATE, StopWatchState.CANCELLED.name)
            action = ACTION_SERVICE_CANCEL
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        return PendingIntent.getActivity(
            context,
            CANCEL_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    fun triggerForegroundService(action: String) {
        Intent(context, StopWatchService::class.java).apply {
            this.action = action
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                context.startService(this)
            }
        }
    }
}

