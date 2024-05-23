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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import database.Session
import database.SessionDatabase
import view.timer.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTimerScreen(navController: NavController,  timerViewModel: TimerViewModel, sessionDatabase: SessionDatabase) {
    val intervals by timerViewModel.intervalsOriginal.collectAsState()
    // WarmUp
    val warmupMinutes by timerViewModel.warmupMinutes.collectAsState()
    val warmupSeconds by timerViewModel.warmupSeconds.collectAsState()
    // Work
    val workMinutes by timerViewModel.workMinutes.collectAsState()
    val workSeconds by timerViewModel.workSeconds.collectAsState()
    // Rest
    val restMinutes by timerViewModel.restMinutes.collectAsState()
    val restSeconds by timerViewModel.restSeconds.collectAsState()
    // Cooldown
    val cooldownMinutes by timerViewModel.cooldownMinutes.collectAsState()
    val cooldownSeconds by timerViewModel.cooldownSeconds.collectAsState()

    val selectedField by timerViewModel.selectedField.collectAsState()
    var isPadVisible by remember { mutableStateOf(false) }
    val sessionName by timerViewModel.sessionName.collectAsState()
    val focusManager = LocalFocusManager.current

    Scaffold (topBar = {
        TopAppBar(
            title = {
                Text(text = "")
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        timerViewModel.resetSetTimer()
                        navController.popBackStack()
                    }.size(32.dp)
                )
            })
    }){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

                // Nombre
                NameInputField(
                    modifier = Modifier,
                    label = "Nombre de la sesión",
                    value = sessionName,
                    selected = selectedField == Field.NAME,
                    onClick = {
                        timerViewModel.selectField(Field.NAME)
                        isPadVisible = false
                    },
                    onValueChange = timerViewModel::setSessionName
                    ,
                    focusManager = focusManager
                )
                Spacer(modifier = Modifier.padding(4.dp))
                // Intérvalos
                IntervalsInputField(
                    modifier = Modifier,
                    label = "Intérvalos",
                    value = intervals.toString(),
                    selected = selectedField == Field.ROUNDS,
                    onClick = {
                        timerViewModel.selectField(Field.ROUNDS)
                        isPadVisible = false
                        focusManager.clearFocus()
                    },
                    timerViewModel = timerViewModel
                )


            // WarmUp and Cooldown times
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                TimeInputField(
                    label = "Calentamiento",
                    value = "$warmupMinutes:$warmupSeconds",
                    selected = selectedField == Field.WARMUP,
                    onClick = {
                        timerViewModel.selectField(Field.WARMUP)
                        isPadVisible = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f)
                )

                TimeInputField(
                    label = "Enfriamiento",
                    value = "$cooldownMinutes:$cooldownSeconds",
                    selected = selectedField == Field.COOLDOWN,
                    onClick = {
                        timerViewModel.selectField(Field.COOLDOWN)
                        isPadVisible = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // Work & Rest times
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                TimeInputField(
                    label = "Trabajo",
                    value = "$workMinutes:$workSeconds",
                    selected = selectedField == Field.WORK,
                    onClick = {
                        timerViewModel.selectField(Field.WORK)
                        isPadVisible = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f)
                )

                TimeInputField(
                    label = "Descanso",
                    value = "$restMinutes:$restSeconds",
                    selected = selectedField == Field.REST,
                    onClick = {
                        timerViewModel.selectField(Field.REST)
                        isPadVisible = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f)
                )
            }



            Spacer(modifier = Modifier.padding(4.dp))

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
                            Field.WARMUP -> {
                                val combined = (warmupMinutes + warmupSeconds + number).takeLast(4)
                                val minutes = combined.take(2)
                                val seconds = combined.takeLast(2)
                                timerViewModel.setTime(Field.WARMUP, minutes, seconds)
                            }
                            Field.COOLDOWN -> {
                                val combined = (cooldownMinutes + cooldownSeconds + number).takeLast(4)
                                val minutes = combined.take(2)
                                val seconds = combined.takeLast(2)
                                timerViewModel.setTime(Field.COOLDOWN, minutes, seconds)
                            }
                            Field.NAME -> {

                            }
                        }
                    },
                    onClearClick = {
                        when (selectedField) {
                            Field.NAME -> {
                                isPadVisible = false
                            }
                            Field.ROUNDS -> {
                                isPadVisible = false
                            }
                            Field.WORK -> {
                                timerViewModel.setTime(Field.WORK, "00", "00")
                            }
                            Field.REST -> {
                                timerViewModel.setTime(Field.REST, "00", "00")

                            }
                            Field.WARMUP -> {
                                timerViewModel.setTime(Field.WARMUP, "00", "00")
                            }
                            Field.COOLDOWN -> {
                                timerViewModel.setTime(Field.COOLDOWN, "00", "00")

                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.padding(4.dp))
            // Botón de empezar
            ElevatedButton(
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    val totalWorkTime = workMinutes.toInt() * 60 + workSeconds.toInt()
                    val totalRestTime = restMinutes.toInt() * 60 + restSeconds.toInt()
                    val totalWarmupTime = warmupMinutes.toInt() * 60 + warmupSeconds.toInt()
                    val totalCooldownTime = cooldownMinutes.toInt() * 60 + cooldownSeconds.toInt()
                    timerViewModel.setTimer(totalRestTime, totalWorkTime, totalWarmupTime, totalCooldownTime)
                    val newSession = Session(
                        sessionName = sessionName,
                        intervals = intervals,
                        warmupTime = totalWarmupTime,
                        workTime = totalWorkTime,
                        restTime = totalRestTime,
                        cooldownTime = totalCooldownTime
                    )
                    timerViewModel.addSession(newSession, sessionDatabase)
                    navController.navigate("TimerScreen")
            }) {
                Text("Iniciar Timer")
            }
        }
    }
}

enum class Field {
    NAME, ROUNDS, WARMUP, WORK, REST, COOLDOWN
}

@Composable
fun TimeInputField(label: String, value: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp).fillMaxWidth()) {
            Row(modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
                ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
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
fun IntervalsInputField(
    modifier: Modifier,
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    timerViewModel: TimerViewModel) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
                ) {
                SquareButton("-") { timerViewModel.removeIntervals() }
                Text(
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    text = value,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                SquareButton("+") { timerViewModel.addIntervals() }
            }

        }
    }
}
@Composable
fun NameInputField(
    modifier: Modifier,
    label: String, value: String,
    selected: Boolean, onClick: () -> Unit,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Center
                ) {
                OutlinedTextField(
                    modifier = modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange,
                    label = { Text(label) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.clearFocus()
                    })
                )
            }

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
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
@Composable
fun SquareButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
            .clip(RectangleShape)
            .background(MaterialTheme.colorScheme.primary, RectangleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}