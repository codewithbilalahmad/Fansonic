package com.muhammad.fansonic

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.muhammad.fansonic.measurement_picker.HeightPickerScreen
import com.muhammad.fansonic.ui.theme.FansonicTheme

class MainActivity : ComponentActivity() {
//    private var isBound by mutableStateOf(false)
//    private lateinit var stopWatchService: StopWatchService
//    private var pendingState: String? = null
//    private var pendingAction: String? = null
//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
//            val binder = service as StopWatchService.StopWatchBinder
//            stopWatchService = binder.getService()
//            isBound = true
//            if (pendingState != null && pendingAction != null) {
//                handleStopWatchNotificationIntent(Intent().apply {
//                    putExtra(STOPWATCH_STATE, pendingState)
//                    action = pendingAction
//                })
//                pendingState = null
//                pendingAction = null
//            }
//        }
//
//
//        override fun onServiceDisconnected(p0: ComponentName?) {
//            isBound = false
//        }
//    }

//    override fun onStart() {
//        super.onStart()
//        Intent(this, StopWatchService::class.java).also { intent ->
//            bindService(intent, connection, BIND_AUTO_CREATE)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ), statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
//        handleStopWatchNotificationIntent(intent = intent)
        setContent {
            FansonicTheme {
                HeightPickerScreen()
//                if (isBound) {
//                    StopWatchScreen(stopWatchService)
//                }
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
//            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
//        handleStopWatchNotificationIntent(intent)
    }

//    override fun onStop() {
//        super.onStop()
//        unbindService(connection)
//        isBound = false
//    }
//
//    private fun handleStopWatchNotificationIntent(intent: Intent) {
//        val state = intent.getStringExtra(STOPWATCH_STATE)
//        val action = intent.action
//        if (state != null && action != null) {
//            if (this::stopWatchService.isInitialized && isBound) {
//                when (action) {
//                    ACTION_SERVICE_CANCEL -> stopWatchService.updateStateFromNotification(
//                        StopWatchState.CANCELLED.name
//                    )
//
//                    ACTION_SERVICE_START -> stopWatchService.updateStateFromNotification(
//                        StopWatchState.STARTED.name
//                    )
//
//                    ACTION_SERVICE_STOP -> stopWatchService.updateStateFromNotification(
//                        StopWatchState.STOPPED.name
//                    )
//                }
//            } else {
//                pendingState = state
//                pendingAction = action
//            }
//        }
//    }
}