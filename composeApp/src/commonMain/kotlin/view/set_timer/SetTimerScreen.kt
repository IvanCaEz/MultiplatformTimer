package view.set_timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedButton
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
import view.timer.TimerViewModel

@Composable
fun SetTimerScreen(navController: NavController,  timerViewModel: TimerViewModel) {
    val intervals by timerViewModel.intervals.collectAsState()
    val restTime by timerViewModel.restTime.collectAsState()
    val workTime by timerViewModel.workTime.collectAsState()

    Scaffold{
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Intérvalos
            Text(text = "Intervalos")
            Row(modifier = Modifier) {

                IconButton(onClick = { timerViewModel.removeIntervals() }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Rest icon")
                }
                Text(text = intervals.toString())
                IconButton(onClick = { timerViewModel.addIntervals() }) {
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Add icon")

                }
            }
            // Botón de empezar
            ElevatedButton(
                onClick = { navController.navigate("TimerScreen")}
            ) {
                Text("Preparado")
            }

        }
    }
}