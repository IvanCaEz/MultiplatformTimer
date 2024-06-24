package view.set_timer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ivancaez.cooltimer.ui.theme.WorkTimeColor
import data.Field
import database.Session
import database.SessionDatabase
import localization.Localization
import view.timer.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetTimerScreen(
    navController: NavController,  timerViewModel: TimerViewModel,
    sessionDatabase: SessionDatabase, localization: Localization
) {
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

    // Control de errores
    val intervalValid by timerViewModel.intervalsValid.collectAsState()
    val workTimeValid by timerViewModel.workTimeValid.collectAsState()
    val restTimeValid by timerViewModel.restTimeValid.collectAsState()
    val canProceed by timerViewModel.canProceed.collectAsState()
    val selectedField by timerViewModel.selectedField.collectAsState()
    var isPadVisible by remember { mutableStateOf(false) }
    val sessionName by timerViewModel.sessionName.collectAsState()
    val focusManager = LocalFocusManager.current
    val verticalSroll = rememberScrollState()

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
                        navController.popBackStack()
                        timerViewModel.resetSetTimer()
                    }.size(32.dp)
                )
            })
    }){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(verticalSroll),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

                // Nombre
                NameInputField(
                    modifier = Modifier,
                    label = localization.getString("session_name"),
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
                    label = localization.getString("intervals"),
                    value = intervals,
                    selected = selectedField == Field.ROUNDS,
                    onClick = {
                        timerViewModel.selectField(Field.ROUNDS)
                        isPadVisible = false
                        focusManager.clearFocus()
                    },
                    onValueChange = { timerViewModel.setIntervals(it) },
                    timerViewModel = timerViewModel,
                    focusManager = focusManager,
                    isValid = intervalValid
                )

            // WarmUp and Cooldown times
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                TimeInputField(
                    label = localization.getString("warmup"),
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
                    label = localization.getString("cooldown"),
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
                    label = localization.getString("work"),
                    value = "$workMinutes:$workSeconds",
                    selected = selectedField == Field.WORK,
                    onClick = {
                        timerViewModel.selectField(Field.WORK)
                        isPadVisible = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f),
                    isValid = workTimeValid
                )

                TimeInputField(
                    label = localization.getString("rest"),
                    value = "$restMinutes:$restSeconds",
                    selected = selectedField == Field.REST,
                    onClick = {
                        timerViewModel.selectField(Field.REST)
                        isPadVisible = true
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.weight(1f),
                    isValid = restTimeValid
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
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ),
                onClick = {
                    val totalWorkTime = workMinutes.toInt() * 60 + workSeconds.toInt()
                    val totalRestTime = restMinutes.toInt() * 60 + restSeconds.toInt()
                    timerViewModel.validateSession(intervals.toInt(), totalWorkTime, totalRestTime)
            }) {
                Text(localization.getString("start_timer"))
            }
        }
        LaunchedEffect(canProceed) {
            if (canProceed) {
                val totalWorkTime = workMinutes.toInt() * 60 + workSeconds.toInt()
                val totalRestTime = restMinutes.toInt() * 60 + restSeconds.toInt()
                val totalWarmupTime = warmupMinutes.toInt() * 60 + warmupSeconds.toInt()
                val totalCooldownTime = cooldownMinutes.toInt() * 60 + cooldownSeconds.toInt()
                var session = timerViewModel.currentSession
                if (session != null) {
                    session.sessionName = sessionName
                    session.intervals = intervals.toInt()
                    session.warmupTime = totalWarmupTime
                    session.workTime = totalWorkTime
                    session.restTime = totalRestTime
                    session.cooldownTime = totalCooldownTime
                } else {
                    session = Session(
                        sessionName = sessionName,
                        intervals = intervals.toInt(),
                        warmupTime = totalWarmupTime,
                        workTime = totalWorkTime,
                        restTime = totalRestTime,
                        cooldownTime = totalCooldownTime
                    )
                }
                timerViewModel.setTimer(session)
                timerViewModel.addSession(session, sessionDatabase)
                navController.navigate("TimerScreen")
            }
        }
    }
}

@Composable
fun TimeInputField(
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    isValid: Boolean = true
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else if (!isValid) WorkTimeColor else Color.Transparent)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntervalsInputField(
    modifier: Modifier,
    label: String,
    value: String,
    selected: Boolean,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,
    timerViewModel: TimerViewModel,
    focusManager: FocusManager,
    isValid: Boolean) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else if (!isValid) WorkTimeColor else Color.Transparent)
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
                SquareButton("-") {
                    timerViewModel.selectField(Field.ROUNDS)
                    timerViewModel.removeIntervals()
                    focusManager.clearFocus()
                }
                BasicTextField2(
                    modifier = modifier.clickable { onClick() }.wrapContentSize(),
                    value = value,
                    onValueChange = onValueChange,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    textStyle = TextStyle(fontSize = 24.sp, textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                        ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number
                        ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.clearFocus()
                    })
                )

                SquareButton("+") {
                    timerViewModel.selectField(Field.ROUNDS)
                    timerViewModel.addIntervals()
                    focusManager.clearFocus()
                }
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
            .clickable {
                onClick()
            }
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
                    singleLine = true,
                    label = { Text(label) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                        ),
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
                        NumberButton("C", textColor = Color.White,
                            backgroundColor = MaterialTheme.colorScheme.secondaryContainer) { onClearClick() }

                    }
                }
            }
        }
    }
}

@Composable
fun NumberButton(number: String,  backgroundColor: Color = MaterialTheme.colorScheme.primary,
                 textColor: Color = MaterialTheme.colorScheme.onPrimary , onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .padding(8.dp)
            .clip(CircleShape)
            .background(backgroundColor, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            fontSize = 24.sp,
            color = textColor
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