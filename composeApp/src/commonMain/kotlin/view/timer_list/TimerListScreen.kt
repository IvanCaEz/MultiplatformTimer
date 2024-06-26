package view.timer_list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ivancaez.cooltimer.ui.theme.CooldownTimeColor
import com.ivancaez.cooltimer.ui.theme.RestTimeColor
import com.ivancaez.cooltimer.ui.theme.WarmupTimeColor
import com.ivancaez.cooltimer.ui.theme.WorkTimeColor
import database.Session
import database.SessionDatabase
import localization.Localization
import session_visualizer.SessionVisualizer
import view.timer.TimerViewModel

@Composable
fun TimerListScreen(
    navController: NavController, timerViewModel: TimerViewModel,
    sessionDatabase: SessionDatabase, localization: Localization,
    sessionVisualizer: SessionVisualizer
){

    val sessions by timerViewModel.sessionList.collectAsState()
    val isLoading by timerViewModel.isLoading.collectAsState()

    val svIsShowing = sessionVisualizer.getPrefs()


    Scaffold(floatingActionButton = {
        Column {
            FloatingActionButton(
                onClick = {
                    navController.navigate("SettingsScreen")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings button"
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("SetUpScreen")
                },
                modifier = Modifier.padding(8.dp),
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add session"
                )
            }
        }
    }) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (sessions.isEmpty()) {
                NoSessionsText(localization)
            } else {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    items(sessions, key = { session -> session.id }) { session ->
                        SessionCard(
                            timerViewModel,
                            session,
                            navController,
                            localization,
                            svIsShowing,
                            sessionDatabase
                        )
                    }
                } // End of LazyColumn
            } // End of session not empty
        } // End of isLoading
    }
}
@Composable
fun NoSessionsText(localization: Localization) {
    Column(
        modifier = Modifier.fillMaxSize().padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = localization.getString("no_sessions"),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun SessionCard(
    timerViewModel: TimerViewModel,
    session: Session,
    navController: NavController,
    localization: Localization,
    svIsShowing: Boolean,
    sessionDatabase: SessionDatabase
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
            .clickable {
                timerViewModel.setTimer(session)
                navController.navigate("TimerScreen")
            },
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            // Info de la sesión
            Column(modifier = Modifier.padding(end = 4.dp).weight(3.5f)) {
                // Nombre de la sesión
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            append(session.sessionName.ifEmpty { "#${session.id}" })
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            append(" - ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = CooldownTimeColor,
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic
                            )
                        ) {
                            val totalTime =
                                session.intervals * (session.restTime + session.workTime) + session.warmupTime!! + session.cooldownTime!!

                            append(timerViewModel.formatTime(totalTime) + if (totalTime > 59) " min" else " s")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = WarmupTimeColor
                )
                // Intervalos
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(localization.getString("intervals") + " ")
                        }
                        append(session.intervals.toString())
                    },
                    fontSize = 18.sp
                )
                // Calentamiento
                if (session.warmupTime != 0) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                append(localization.getString("warmup_time") + " ")
                            }
                            append(timerViewModel.formatTime(session.warmupTime!!) + if (session.warmupTime!! > 59) " min" else " s")
                        },
                        fontSize = 18.sp
                    )
                }
                // Trabajo
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(localization.getString("work_time") + " ")
                        }
                        append(timerViewModel.formatTime(session.workTime) + if (session.workTime > 59) " min" else " s")
                    },
                    fontSize = 18.sp
                )
                // Descanso
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append(localization.getString("rest_time") + " ")
                        }
                        append(timerViewModel.formatTime(session.restTime) + if (session.restTime > 59) " min" else " s")
                    },
                    fontSize = 18.sp
                )
                // Enfriamiento
                if (session.cooldownTime != 0) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                append(localization.getString("cooldown_time") + " ")
                            }
                            append(timerViewModel.formatTime(session.cooldownTime!!) + if (session.cooldownTime!! > 59) " min" else " s")
                        },
                        fontSize = 18.sp
                    )
                }
                if (svIsShowing) {
                    SessionVisualizer(
                        warmupTime = session.warmupTime ?: 0,
                        workTime = session.workTime,
                        restTime = session.restTime,
                        intervals = session.intervals,
                        cooldownTime = session.cooldownTime ?: 0,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            }
            // Botones de editar y borrar
            Column(
                modifier = Modifier.padding(end = 4.dp).weight(0.5f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    modifier = Modifier.clip(CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        timerViewModel.setSession(session)
                        navController.navigate("SetUpScreen")
                    }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit session"
                    )
                }
                IconButton(
                    modifier = Modifier.clip(CircleShape),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    onClick = {
                        timerViewModel.deleteSession(session, sessionDatabase)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete session"
                    )
                }
            }
        }
    }
}


@Composable
fun SessionVisualizer(
    warmupTime: Int,
    workTime: Int,
    restTime: Int,
    intervals: Int,
    cooldownTime: Int,
    modifier: Modifier = Modifier
) {
    val totalTime = warmupTime + (workTime + restTime) * intervals + cooldownTime

    Canvas(modifier = modifier.fillMaxWidth().height(20.dp)) {
        val widthPerSecond = size.width / totalTime

        var currentX = 0f

        // Warmup time
        if (warmupTime > 0){
            drawRect(
                color = WarmupTimeColor,
                topLeft = Offset(currentX, 0f),
                size = Size(warmupTime * widthPerSecond, size.height)
            )
            currentX += warmupTime * widthPerSecond
        }

        // Intervals
        for (i in 0 until intervals) {
            // Work time
            drawRect(
                color = WorkTimeColor,
                topLeft = Offset(currentX, 0f),
                size = Size(workTime * widthPerSecond, size.height)
            )
            currentX += workTime * widthPerSecond

            // Rest time
            drawRect(
                color = RestTimeColor,
                topLeft = Offset(currentX, 0f),
                size = Size(restTime * widthPerSecond, size.height)
            )
            currentX += restTime * widthPerSecond
        }

        // Cooldown time
        if (cooldownTime > 0){
            drawRect(
                color = CooldownTimeColor,
                topLeft = Offset(currentX, 0f),
                size = Size(cooldownTime * widthPerSecond, size.height)
            )
        }
    }
}

