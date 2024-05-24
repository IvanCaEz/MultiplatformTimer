import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import database.getSessionDatabase
import di.KoinInitializer
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNAuthorizationOptions

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
    App(sessionDatabase)
}