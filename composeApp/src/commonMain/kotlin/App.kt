
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import database.SessionDatabase
import localization.Localization
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.currentKoinScope
import ui.theme.darkScheme
import ui.theme.lightScheme
import view.set_timer.SetTimerScreen
import view.settings.SettingsScreen
import view.timer.TimerScreen
import view.timer.TimerViewModel
import view.timer_list.TimerListScreen

@Composable
@Preview
fun App(sessionDatabase: SessionDatabase, context: Any? = null, localization: Localization) {

    val colors by mutableStateOf(if (isSystemInDarkTheme()) darkScheme else lightScheme)

    MaterialTheme(colorScheme = colors) {
        val navHost = rememberNavController()

        KoinContext {
            val timerViewModel = koinViewModel<TimerViewModel>()
            timerViewModel.loadSessions(sessionDatabase)

            NavHost(navController = navHost, startDestination = "TimerListScreen") {

                composable("TimerListScreen") {

                    TimerListScreen(navHost, timerViewModel, sessionDatabase, localization)
                }
                composable("SetUpScreen") {
                    SetTimerScreen(navHost, timerViewModel, sessionDatabase, localization)
                }
                composable("SettingsScreen") {
                    SettingsScreen(navHost,sessionDatabase, localization, timerViewModel)
                }

                composable("TimerScreen", deepLinks = listOf(
                    NavDeepLink("CoolTimer://TimerScreen")
                ))  {
                    TimerScreen(navHost, timerViewModel, context, localization)
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
