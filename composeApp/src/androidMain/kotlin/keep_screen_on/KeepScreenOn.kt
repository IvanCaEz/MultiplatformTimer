package keep_screen_on
import android.app.Activity
import android.view.WindowManager
actual class KeepScreenOn(private val activity: Activity) {
    actual fun enable(){
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }
    actual fun disable(){
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

}