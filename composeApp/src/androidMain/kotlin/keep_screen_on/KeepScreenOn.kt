package keep_screen_on
import android.content.Context
import android.util.Log
import android.view.WindowManager
import org.ivancaez.cooltimer.utils.getActivity

actual fun keepScreenOn(context: Any?, keepScreenOn: Boolean) {
    if (context !is Context) return

    val activity = context.getActivity()

    activity?.let {
        if (keepScreenOn) {

            it.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            Log.d("KeepScreenOn", "Screen will stay on")

        } else {
            it.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            Log.d("KeepScreenOn", "Screen can turn off")
        }
    } ?: Log.d("KeepScreenOn", "Activity is null")
}