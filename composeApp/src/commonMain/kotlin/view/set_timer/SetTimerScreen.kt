package view.set_timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import view.timer.TimerViewModel

@Composable
fun SetTimerScreen(navController: NavController,  timerViewModel: TimerViewModel) {
    val intervals by timerViewModel.intervalsOriginal.collectAsState()
    val workMinutes by timerViewModel.workMinutes.collectAsState()
    val workSeconds by timerViewModel.workSeconds.collectAsState()
    val restMinutes by timerViewModel.restMinutes.collectAsState()
    val restSeconds by timerViewModel.restSeconds.collectAsState()
    var selectedField by remember { mutableStateOf(Field.ROUNDS) }
    var isPadVisible by remember { mutableStateOf(false) }


    Scaffold{
        Column(modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Intérvalos
            Box(modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (selectedField == Field.ROUNDS) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                .clickable {
                    selectedField = Field.ROUNDS
                    isPadVisible = false
                }
            ) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                ){
                    Text(text = "Intérvalos", style = MaterialTheme.typography.headlineMedium)
                }
                Row(modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                    ) {
                    Column(
                        modifier = Modifier.weight(2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        Text(text = intervals.toString(), style = MaterialTheme.typography.headlineMedium)
                    }
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.End
                    ) {
                        NumberButton("+") { timerViewModel.addIntervals() }
                        Spacer(modifier = Modifier.padding(4.dp))
                        NumberButton("-") { timerViewModel.removeIntervals() }
                    }
                }
            }

            TimeInputField(
                label = "Tiempo de Trabajo",
                value = "$workMinutes:$workSeconds",
                selected = selectedField == Field.WORK,
                onClick = {
                    selectedField = Field.WORK
                    isPadVisible = true
                }
            )

            Spacer(modifier = Modifier.padding(4.dp))

            TimeInputField(
                label = "Tiempo de Descanso",
                value = "$restMinutes:$restSeconds",
                selected = selectedField == Field.REST,
                onClick = {
                    selectedField = Field.REST
                    isPadVisible = true
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            AnimatedVisibility(isPadVisible){
                NumberPad(
                    onNumberClick = { number ->
                        when (selectedField) {
                            Field.ROUNDS -> {
                            }
                            Field.WORK -> {
                                val combined = (workMinutes + workSeconds + number).takeLast(4)
                                val minutes = combined.take(2)
                                val seconds = combined.takeLast(2)
                                timerViewModel.setTime(Field.WORK, minutes, seconds)
                            }
                            Field.REST -> {
                                val combined = (restMinutes + restSeconds + number).takeLast(4)
                                val minutes = combined.take(2)
                                val seconds = combined.takeLast(2)
                                timerViewModel.setTime(Field.REST, minutes, seconds)
                            }
                        }
                    },
                    onClearClick = {
                        when (selectedField) {
                            Field.ROUNDS -> {
                                isPadVisible = false
                            }
                            Field.WORK -> {
                                timerViewModel.setTime(Field.WORK, "00", "00")
                            }
                            Field.REST -> {
                                timerViewModel.setTime(Field.REST, "00", "00")

                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            // Botón de empezar
            ElevatedButton(onClick = {
                val totalWorkTime = workMinutes.toInt() * 60 + workSeconds.toInt()
                val totalRestTime = restMinutes.toInt() * 60 + restSeconds.toInt()
                timerViewModel.setTimer(totalRestTime, totalWorkTime)
                navController.navigate("TimerScreen")
            }) {
                Text("Iniciar Timer")
            }
        }
    }
}

enum class Field {
    ROUNDS, WORK, REST
}

@Composable
fun TimeInputField(label: String, value: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = value,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun NumberPad(onNumberClick: (String) -> Unit, onClearClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numbers = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("0", "C")
        )

        for (row in numbers) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(2.dp)
            ) {
                for (number in row) {
                    if (number != "C"){
                        NumberButton(number) { onNumberClick(number) }
                    } else {
                        NumberButton("C") { onClearClick() }

                    }
                }
            }
        }
    }
}

@Composable
fun NumberButton(number: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(8.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 24.sp,
            color = Color.White
        )
    }
}