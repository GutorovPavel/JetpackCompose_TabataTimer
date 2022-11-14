package com.example.tabatatimer

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Timer(
    workTime: Long = 5L * 1000L,
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
    var currentTime by remember {
        mutableStateOf(workTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    var isPause by remember {
        mutableStateOf(false)
    }

    /////////////// ANIMATE TIMER /////////////////

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (isTimerRunning && currentTime > 0L) {
            delay(100L)
            currentTime -= 100L
            value = 
                if(!isPause) { 
                    currentTime / workTime.toFloat()
                } else {
                    currentTime / pauseTime.toFloat()
                }
        }
        if (currentTime == 0L) {
            isPause = !isPause
            currentTime += if (isPause) { pauseTime } else { workTime }
        }
    }

    ///////////////////////////////////////////////

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (currentTime <= 0L) {
                            currentTime = workTime
                            isTimerRunning = true
                        } else {
                            if (!isPause) {
                                isTimerRunning = !isTimerRunning
                            }
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
                        if (isPause) {
                            pauseBarColor
                        } else {
                            Color.Red
                        }
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

@Preview
@Composable
fun PreviewTimer() {
    Timer(
        workTime = 100L * 1000L,
        modifier = Modifier.size(200.dp)
    )
}