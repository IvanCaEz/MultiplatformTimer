import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import database.getSessionDatabase
import di.KoinInitializer
import localization.LocalizationIos
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNAuthorizationOptions
import session_visualizer.SessionVisualizerIos

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer().init()
    }

) {
    // Request notification permission
    UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
        options = UNAuthorizationOptions(UNAuthorizationOptionAlert or UNAuthorizationOptionSound),
        completionHandler = { granted, error ->
            if (!granted) {
                println("Notification permission denied")
            }
        })
    val sessionDatabase = remember { getSessionDatabase() }
    val sessionVisualizer = remember { SessionVisualizerIos()}
    val localization = remember { LocalizationIos() }
    App(sessionDatabase = sessionDatabase, localization = localization, sessionVisualizer = sessionVisualizer)
}