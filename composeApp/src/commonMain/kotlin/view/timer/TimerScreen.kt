package view.timer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navController: NavController, timerViewModel: TimerViewModel) {
    val intervals by timerViewModel.intervals.collectAsState()
    val isWorkTime by timerViewModel.isWorkTime.collectAsState()
    val hasStarted by timerViewModel.hasStarted.collectAsState()
    val isPaused by timerViewModel.isPaused.collectAsState()
    val remainingTime by timerViewModel.remainingTime.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "")
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }.size(32.dp)
                )
            })
    }){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Intérvalo $intervals", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = if (isWorkTime) "Tiempo de trabajo" else "Tiempo de descanso",
                style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.padding(20.dp))

            Box(contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentSize()
            ) {
                Text(text = remainingTime.toString(),
                    fontSize = 42.sp)

                CoolCircularProgressBar(
                    size = 250.dp,
                    strokeWidth = 16.dp,
                    progress = if (isWorkTime) {
                        remainingTime.toFloat() / timerViewModel.workTime.value
                    } else {
                        remainingTime.toFloat() / timerViewModel.restTime.value
                    },
                    startAngle = -215f,
                    backgroundArcColor = Color.LightGray,
                    progressArcColor1 = if (isWorkTime) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                    progressArcColor2 = if (isWorkTime) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    animationOn = hasStarted,
                )
            }
            Spacer(modifier = Modifier.padding(12.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                ElevatedButton(onClick = {
                    if(hasStarted && !isPaused) timerViewModel.pauseTimer()
                    else if (hasStarted && isPaused) timerViewModel.resumeTimer()
                    else timerViewModel.startTimer()
                }
                ) {
                    if (hasStarted && !isPaused) Text(text = "Pausar")
                    else if (hasStarted && isPaused) Text(text = "Reanudar")
                    else Text(text = "Empezar")
                }
                AnimatedContent(isPaused){
                    if (isPaused) {
                        Row(modifier = Modifier.padding(start = 10.dp)){
                            ElevatedButton(onClick = { timerViewModel.stopTimer() },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                )) {
                                Text(text = "Detener")
                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun CoolCircularProgressBar(
    size: Dp = 96.dp,
    strokeWidth: Dp = 12.dp,
    progress: Float = 0f,
    startAngle: Float = -215f,
    backgroundArcColor: Color = Color.LightGray,
    progressArcColor1: Color = Color.Blue,
    progressArcColor2: Color = progressArcColor1,
    animationOn: Boolean = false,
) {

    // Progress Animation Implementation
    val currentProgress = remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress.floatValue,
        animationSpec = if (animationOn) tween(1000) else tween(0),
        label = "Progress Animation"
    )
    LaunchedEffect(animationOn, progress) {
        if (animationOn) {
            progressFlow(progress).collect { value ->
                currentProgress.floatValue = value
            }
        } else {
            currentProgress.floatValue = progress
        }
    }

    Canvas(modifier = Modifier.size(size)) {
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(progressArcColor1, progressArcColor2)
        )
        val strokeWidthPx = strokeWidth.toPx()
        val arcSize = size.toPx() - strokeWidthPx

        drawArc(
            brush = gradientBrush,
            startAngle = startAngle,
            sweepAngle = animatedProgress * 250,
            useCenter = false,
            topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
            size = Size(arcSize, arcSize),
            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
        )

    }
}

/**
 * A function that creates a Flow emitting Float values, simulating a progress animation.
 *
 * @param targetProgress The final progress value to reach, default is 1f.
 * @param step The increment for each emitted progress value, default is 0.01f.
 * @return A Flow emitting Float values representing the progress.
 */
fun progressFlow(targetProgress: Float = 1f, step: Float = 0.01f): Flow<Float> {
    return flow {
        var progress = 0f
        while (progress <= targetProgress) {
            emit(progress)
            progress += step
        }
    }
}
