package view.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun TimerScreen(navController: NavController, timerViewModel: TimerViewModel) {
    val timer by timerViewModel.timer.collectAsState()
    val intervals by timerViewModel.intervals.collectAsState()
    val restTimeTimer by timerViewModel.restTimeTimer.collectAsState()
    val workTimeTimer by timerViewModel.workTimeTimer.collectAsState()
    val isWorkTime by timerViewModel.isWorkTime.collectAsState()
    val hasStarted by timerViewModel.hasStarted.collectAsState()


    Scaffold{
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Intérvalo: $intervals")
            Text(text = if (isWorkTime) "Tiempo de trabajo: $workTimeTimer" else "Tiempo de descanso: $restTimeTimer")
            IconButton(onClick = { timerViewModel.starTimer() }) {
                Icon(imageVector = if(hasStarted) Icons.Default.PlayArrow else Icons.Default.Clear, contentDescription = "Empezar")
            }

        }
    }

}