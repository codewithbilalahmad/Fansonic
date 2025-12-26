package com.muhammad.fansonic

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.muhammad.fansonic.audioPlayer.AudioPlayerScreen
import com.muhammad.fansonic.calendar_topbar.CalendarTopBarScreen
import com.muhammad.fansonic.carousel.CarouselScreen
import com.muhammad.fansonic.clock.ClockScreen
import com.muhammad.fansonic.cornered_box.CorneredBoxScreen
import com.muhammad.fansonic.counter.CounterScreen
import com.muhammad.fansonic.depth_card.ColorScreen
import com.muhammad.fansonic.depth_card.DepthCardScreen
import com.muhammad.fansonic.explodable_chips.ExplodableClipScreen
import com.muhammad.fansonic.folder_picker.FolderPickerScreen
import com.muhammad.fansonic.foreground_service.service.StopWatchService
import com.muhammad.fansonic.foreground_service.service.StopWatchState
import com.muhammad.fansonic.foreground_service.ui.StopWatchScreen
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_CANCEL
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_START
import com.muhammad.fansonic.foreground_service.util.Constants.ACTION_SERVICE_STOP
import com.muhammad.fansonic.foreground_service.util.Constants.STOPWATCH_STATE
import com.muhammad.fansonic.glass.GlassCardScreen
import com.muhammad.fansonic.glitchEffect.GlitchEffectScreen
import com.muhammad.fansonic.grid_background.GridBackgroundScreen
import com.muhammad.fansonic.heightPicker.HeightPickerScreen
import com.muhammad.fansonic.html_text.HtmlTextScreen
import com.muhammad.fansonic.month_calendar.MonthCalendarScreen
import com.muhammad.fansonic.physicsButton.PhysicsButtonScreen
import com.muhammad.fansonic.pulseAnimation.PulseAnimationScreen
import com.muhammad.fansonic.raised_button.RaisedButtonScreen
import com.muhammad.fansonic.rating.RatingStarsScreen
import com.muhammad.fansonic.shadows.ShadowsScreen
import com.muhammad.fansonic.shared_element.SharedElementScreen
import com.muhammad.fansonic.snake_game.ui.SnakeGameScreen
import com.muhammad.fansonic.stackable_item.StackableItemScreen
import com.muhammad.fansonic.step_progress.ProgressScreen
import com.muhammad.fansonic.typing.TypingTextScreen
import com.muhammad.fansonic.ui.theme.FansonicTheme
import com.muhammad.fansonic.week_calendar.WeekCalendarScreen

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