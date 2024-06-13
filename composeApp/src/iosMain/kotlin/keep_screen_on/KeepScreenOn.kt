package keep_screen_on
import platform.UIKit.UIApplication

actual fun keepScreenOn(context: Any?, keepScreenOn: Boolean) {
    UIApplication.shared.isIdleTimerDisabled = keepScreenOn
}