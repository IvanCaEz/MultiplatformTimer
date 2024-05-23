package view.timer_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import database.SessionDatabase
import view.timer.TimerViewModel

@Composable
fun TimerListScreen(navController: NavController, timerViewModel: TimerViewModel, sessionDatabase: SessionDatabase) {

    val sessions by sessionDatabase.sessionDao().getAllSessions()
        .collectAsState(initial = emptyList())
    timerViewModel.setSessionList(sessions.toMutableList())

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navController.navigate("SetUpScreen")
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add session"
            )
        }
    }) {
        if (sessions.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No hay sesiones guardadas, crea una",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.padding(8.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items(sessions.size) { index ->
                    val session = sessions[index]
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                            .wrapContentHeight()
                            .clickable {
                                timerViewModel.setSession(session)
                                navController.navigate("TimerScreen")
                            },
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            // Info de la sesión
                            Column(modifier = Modifier.padding(end = 4.dp).weight(4f)) {
                                // Nombre de la sesión
                                Text(
                                    text = session.sessionName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                // Intérvalos
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(
                                                fontWeight = FontWeight.Bold,
                                            )
                                        ) {
                                            append("Intérvalos: ")
                                        }
                                        append(session.intervals.toString())
                                    },
                                    fontSize = 18.sp
                                )
                                // Calentamiento
                                if (session.warmupTime != 0){
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            ) {
                                                append("Calentamiento: ")
                                            }
                                            append(timerViewModel.formatTime(session.warmupTime!!))
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
                                            append("Tiempo de trabajo: ")
                                        }
                                        append(timerViewModel.formatTime(session.workTime))
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
                                            append("Tiempo de descanso: ")
                                        }
                                        append(timerViewModel.formatTime(session.restTime))
                                    },
                                    fontSize = 18.sp
                                )
                                // Enfriamiento
                                if (session.cooldownTime != 0){
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            ) {
                                                append("Enfriamiento: ")
                                            }
                                            append(timerViewModel.formatTime(session.cooldownTime!!))
                                        },
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            // Botones de editar y borrar
                            Column(modifier = Modifier.padding(end = 4.dp).weight(0.5f),
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
                                    }){
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit session")
                                }
                                IconButton(
                                    modifier = Modifier.clip(CircleShape),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    onClick = {
                                        timerViewModel.deleteSession(session, sessionDatabase)
                                    }){
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete session")
                                }

                            }


                        }

                    }
                }

            }

        }
    }
}