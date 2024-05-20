package soundplayer
import platform.AVFoundation.*
import platform.Foundation.NSBundle
actual class SoundPlayer {
    private var audioPlayer: AVAudioPlayer? = null
    actual fun playSound(resourceName: String) {
        val url = NSBundle.mainBundle.URLForResource(resourceName, "mp3")
        if (url != null) {
            audioPlayer = AVAudioPlayer(contentsOfURL = url, error = null)
            audioPlayer?.play()
        }
    }

    actual fun stopSound() {
        audioPlayer?.stop()
        audioPlayer = null
    }
}