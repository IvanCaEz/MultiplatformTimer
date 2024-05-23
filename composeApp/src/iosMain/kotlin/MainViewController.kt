import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import database.getSessionDatabase
import di.KoinInitializer

fun MainViewController() = ComposeUIViewController(
    configure = {
        KoinInitializer().init()
    }

) {
    val sessionDatabase = remember { getSessionDatabase() }
    App(sessionDatabase)
}