package com.example.clocksample

import android.graphics.BlurMaskFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.clocksample.ui.theme.AnalogClockHourHandColor
import com.example.clocksample.ui.theme.AnalogClockInnerBoxColor
import com.example.clocksample.ui.theme.AnalogClockInnerBoxShadow
import com.example.clocksample.ui.theme.AnalogClockMinuteHandColor
import com.example.clocksample.ui.theme.AnalogClockOuterBoxColor
import com.example.clocksample.ui.theme.AnalogClockOuterBoxShadow1
import com.example.clocksample.ui.theme.AnalogClockOuterBoxShadow2
import com.example.clocksample.ui.theme.AnalogClockOuterBoxShadow3
import com.example.clocksample.ui.theme.AnalogClockSecondHandColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.min


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var hour by remember { mutableStateOf(0) }
            var minute by remember { mutableStateOf(0) }
            var second by remember { mutableStateOf(0) }
            LaunchedEffect(Unit) {
                while (true) {
                    val cal = Calendar.getInstance()
                    hour = cal.get(Calendar.HOUR)
                    minute = cal.get(Calendar.MINUTE)
                    second = cal.get(Calendar.SECOND)
                    delay(1000)
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)

            ){
                AnalogClockComponent(hour = hour, minute = minute, second = second)
            }
        }
    }
}

@Composable
fun AnalogClockComponent(
    hour: Int,
    minute: Int,
    second: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val blurRadius = remember { Animatable(2f) }  // Adjusted blur radius

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            blurRadius.animateTo(
                targetValue = 10f,  // Adjusted blur radius
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize(0.7f)
            .aspectRatio(0.5f)
            .shadowCircular(
                offsetX = 0.dp,
                offsetY = 0.dp,
                blurRadius = blurRadius.value.dp,
                gradientColors = listOf(
                    AnalogClockOuterBoxShadow1.copy(0.8f),
                    AnalogClockOuterBoxShadow2,
                    AnalogClockOuterBoxShadow3)
            )
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.78f)
                .aspectRatio(1f)
                .shadowCircular(
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blurRadius = 10.dp,
                    color = AnalogClockInnerBoxShadow
                )
                .clip(CircleShape)
                .background(AnalogClockInnerBoxColor)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = min(size.width, size.height) * 1f  // for dots
                val radius = diameter / 2

                repeat(4) {
                    val start = center - Offset(0f, radius)
                    val end = start + Offset(0f, radius / 40f)
                    rotate(it / 4f * 360) {
                        drawLine(
                            color = Color.White,
                            start = start,
                            end = end,
                            strokeWidth = 5.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }

                val secondRatio = second / 60f
                val minuteRatio = minute / 60f
                val hourRatio = hour / 12f

                rotate(hourRatio * 360, center) {
                    drawLine(
                        color = AnalogClockHourHandColor,
                        start = center - Offset(0f, radius * 0.4f),
                        end = center + Offset(0f, radius * 0.1f),
                        strokeWidth = 3.8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                rotate(minuteRatio * 360, center) {
                    drawLine(
                        color = AnalogClockMinuteHandColor,
                        start = center - Offset(0f, radius * 0.6f),
                        end = center + Offset(0f, radius * 0.1f),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }

                rotate(secondRatio * 360, center) {
                    drawLine(
                        color = AnalogClockSecondHandColor,
                        start = center - Offset(0f, radius * 0.7f),
                        end = center + Offset(0f, radius * 0.1f),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                // center dot in the middle
                drawCircle(
                    color = AnalogClockSecondHandColor,
                    radius = 4.dp.toPx(),
                    center = center
                )
            }
        }
    }
}

fun Modifier.shadowCircular(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    gradientColors: List<Color> = listOf(Color.Transparent, Color.Transparent)
) = drawBehind {
    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()
    if (blurRadius != 0.dp) {
        frameworkPaint.maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.OUTER)
    }
    val gradient = SweepGradientShader(
        colors = gradientColors,
        center = Offset(size.width / 2 + offsetX.toPx(), size.height / 2 + offsetY.toPx())
    )
    paint.shader = gradient
    frameworkPaint.color = color.toArgb()

    drawIntoCanvas { canvas ->
        canvas.drawCircle(
            center = Offset(x = center.x , y = center.y ),
            radius = size.width / 2,
            paint = paint
        )
    }
}
