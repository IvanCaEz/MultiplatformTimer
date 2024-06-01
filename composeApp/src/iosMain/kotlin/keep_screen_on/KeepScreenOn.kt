package keep_screen_on
import platform.UIKit.UIApplication

actual class KeepScreenOn {
    actual fun enable() {
        UIApplication.sharedApplication.idleTimerDisabled = true
    }

    actual fun disable() {
        UIApplication.sharedApplication.idleTimerDisabled = false
    }
}