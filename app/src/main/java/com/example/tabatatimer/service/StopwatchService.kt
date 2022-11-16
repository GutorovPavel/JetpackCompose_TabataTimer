package com.example.tabatatimer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_CANCEL
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_START
import com.example.tabatatimer.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.tabatatimer.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.tabatatimer.util.Constants.NOTIFICATION_ID
import com.example.tabatatimer.util.Constants.STOPWATCH_STATE

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@ExperimentalAnimationApi
@AndroidEntryPoint
class StopwatchService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = StopwatchBinder()

    var currentState = mutableStateOf(StopwatchState.Idle)
        private set

    var currentTime = mutableStateOf(0L)
        private set

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(STOPWATCH_STATE)) {
            StopwatchState.Started.name -> {
                startForegroundService()
                startTimer(10000L) { currentTime ->
                    updateNotification(currentTime = currentTime)
                }
            }
            StopwatchState.Canceled.name -> {
                cancelStopwatch()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    startForegroundService()
                    startTimer { currentTime ->
                        updateNotification(currentTime = currentTime)
                    }                }
                ACTION_SERVICE_CANCEL -> {
                    cancelStopwatch()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private var timer: CountDownTimer? = null

    private fun startTimer(totalTime: Long = 10000L, onTick: (currentTime: Long) -> Unit) {
        timer?.cancel()
        currentState.value = StopwatchState.Started
        timer = object : CountDownTimer(totalTime, 1) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
                onTick(currentTime.value)
            }

            override fun onFinish() {
                startTimer { currentTime ->
                    updateNotification(currentTime = currentTime)
                }
            }

        }.start()
    }

    private fun cancelStopwatch() {
        timer?.cancel()
        currentState.value = StopwatchState.Idle
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
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotification(currentTime: Long) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                if (currentTime / 1000L / 60L < 10L) { "0" } else { "" } +
                (currentTime / 1000L / 60L).toString() + ":" +
                if (currentTime / 1000L % 60L < 10L) { "0" } else { "" } +
                (currentTime / 1000L % 60L).toString()
            ).build()
        )
    }

//    private fun setStopButton() {
//        notificationBuilder.mActions.removeAt(0)
//        notificationBuilder.mActions.add(
//            0,
//            NotificationCompat.Action(
//                0,
//                "Stop",
//                ServiceHelper.stopPendingIntent(this)
//            )
//        )
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//    }
//
//    private fun setResumeButton() {
//        notificationBuilder.mActions.removeAt(0)
//        notificationBuilder.mActions.add(
//            0,
//            NotificationCompat.Action(
//                0,
//                "Resume",
//                ServiceHelper.resumePendingIntent(this)
//            )
//        )
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//    }


    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }
}

enum class StopwatchState {
    Idle,
    Started,
    Stopped,
    Canceled
}