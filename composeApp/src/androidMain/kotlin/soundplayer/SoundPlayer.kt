package soundplayer

import android.media.MediaPlayer
import org.ivancaez.cooltimer.CoolTimerApp

actual class SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    actual fun playSound(resourceName: String) {
        val resId = CoolTimerApp.appContext.resources?.getIdentifier(resourceName, "raw", CoolTimerApp.appContext.packageName)
        if (resId != null && resId != 0) {
            mediaPlayer = MediaPlayer.create(CoolTimerApp.appContext, resId)
            mediaPlayer?.start()
        }
    }

    actual fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}