
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import database.SessionDatabase
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope
import ui.theme.darkScheme
import ui.theme.lightScheme
import view.set_timer.SetTimerScreen
import view.timer.TimerScreen
import view.timer.TimerViewModel
import view.timer_list.TimerListScreen

@Composable
@Preview
fun App(sessionDatabase: SessionDatabase) {

    val colors by mutableStateOf(if (isSystemInDarkTheme()) darkScheme else lightScheme)

    MaterialTheme(colorScheme = colors) {
        val navHost = rememberNavController()
        //val sessions by sessionDao.getAllSessions().collectAsState(initial = emptyList())
        KoinContext {
            val timerViewModel = koinViewModel<TimerViewModel>()



            NavHost(navController = navHost, startDestination = "TimerListScreen") {

                composable("TimerListScreen") {
                    TimerListScreen(navHost, timerViewModel, sessionDatabase)
                }
                composable("SetUpScreen") {
                    SetTimerScreen(navHost, timerViewModel, sessionDatabase)
                }

                composable("TimerScreen") {
                    TimerScreen(navHost, timerViewModel)
                }
            }


        }
    }


}

@Composable
inline fun <reified T: ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get()
    }
}
