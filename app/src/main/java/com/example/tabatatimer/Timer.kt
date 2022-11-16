package com.example.tabatatimer


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tabatatimer.service.ServiceHelper
import com.example.tabatatimer.service.StopwatchService
import com.example.tabatatimer.service.StopwatchState
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_CANCEL
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_START
import com.example.tabatatimer.util.Constants.ACTION_SERVICE_STOP


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Timer(
    stopwatchService: StopwatchService,
    workTime: Long = 10L * 1000L,
    pauseTime: Long = 3L * 1000L,
    inactiveBarColor: Color = MaterialTheme.colorScheme.inverseSurface,
    activeBarColor: Color = MaterialTheme.colorScheme.primary,
    pauseBarColor: Color = Color.Yellow,
    modifier: Modifier = Modifier,
    initValue: Float = 1f,
    strokeWidth: Dp = 15.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var value by remember {
        mutableStateOf(initValue)
    }

    val context = LocalContext.current
    var currentTime by stopwatchService.currentTime
    var currentState by stopwatchService.currentState

    value = (currentTime / workTime).toFloat()

    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    var isPause by remember {
        mutableStateOf(false)
    }


    /////////////// ANIMATE TIMER /////////////////
//
//    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
//        if (isTimerRunning && currentTime > 0L) {
//            delay(100L)
//            currentTime -= 100L
//            value =
//                if(!isPause) {
//                    currentTime / workTime.toFloat()
//                } else {
//                    currentTime / pauseTime.toFloat()
//                }
//        }
//        if (currentTime == 0L) {
//            isPause = !isPause
//            currentTime += if (isPause) { pauseTime } else { workTime }
//        }
//    }
    ///////////////////////////////////////////////

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (currentTime <= 0L) {
//                            currentTime = workTime
                            isTimerRunning = true
                            ServiceHelper.triggerForegroundService(
                                context = context,
                                action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                                else ACTION_SERVICE_START
                            )
//                                if(!isPause) {
//                                    (currentTime / workTime).toFloat()
//                                } else {
//                                    (currentTime / pauseTime).toFloat()
//                                }
                        } else {
//                           if (!isPause) {
//                                isTimerRunning = !isTimerRunning
//                            }
//                            ServiceHelper.triggerForegroundService(
//                                context = context, action = ACTION_SERVICE_CANCEL
//                            )
//                            isTimerRunning = !isTimerRunning
                        }
                    },
                    onLongPress = {
                        currentTime = workTime
                        isTimerRunning = false
                        isPause = false
                        value = workTime.toFloat()
                    }
                )
            }
    ) {
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -270f,
                sweepAngle = 360f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color =
                (
                    if (!isTimerRunning) {
                        activeBarColor
                    } else {
//                        if (isPause) {
//                            pauseBarColor
//                        } else {
                            Color.Red
//                        }
                    }
                ) as Color,
                startAngle = -270f,
                sweepAngle = 360f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text =
                if (currentTime / 1000L / 60L < 10L) { "0" } else { "" } +
                (currentTime / 1000L / 60L).toString() + ":" +
                if (currentTime / 1000L % 60L < 10L) { "0" } else { "" } +
                (currentTime / 1000L % 60L).toString(),

            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
