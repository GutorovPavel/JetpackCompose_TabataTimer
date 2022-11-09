package com.example.tabatatimer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
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
    totalTime: Long,
    inactiveBarColor: Color,
    modifier: Modifier = Modifier,
    initValue: Float = 1f,
    strokeWidth: Dp = 8.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableStateOf(initValue)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    /////////////// ANIMATE TIMER /////////////////

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }

    ///////////////////////////////////////////////

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        if (currentTime <= 0L) {
                            currentTime = totalTime
                            isTimerRunning = true
                        } else {
                            isTimerRunning = !isTimerRunning
                        }
                    },
                    onLongPress = {
                        currentTime = totalTime
                        isTimerRunning = false
                        value = totalTime.toFloat()
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
                if (!isTimerRunning || currentTime <= 0L) {
                    Color.Cyan
                } else {
                    Color.Red
                },
                startAngle = -270f,
                sweepAngle = 360f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
//        Button(
//            onClick = {
//                      if (currentTime <= 0L) {
//                          currentTime = totalTime
//                          isTimerRunning = true
//                      } else {
//                          isTimerRunning = !isTimerRunning
//                      }
//            },
//            modifier = Modifier.align(Alignment.BottomCenter),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = if (!isTimerRunning || currentTime <= 0L) {
//                    Color.Cyan
//                } else {
//                    Color.Red
//                }
//            )
//        ) {
//            Text(
//                text = if(isTimerRunning && currentTime >= 0L) "Stop"
//                else if(!isTimerRunning && currentTime >= 0L) "Start"
//                else "Restart"
//            )
//        }
    }
}

@Preview
@Composable
fun PreviewTimer() {
    Timer(
        totalTime = 100L * 1000L,
        inactiveBarColor = Color.DarkGray,
//        activeBarColor = Color.Cyan,
        modifier = Modifier.size(200.dp)
    )
}