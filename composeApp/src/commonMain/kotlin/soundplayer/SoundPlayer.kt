package soundplayer

expect class SoundPlayer() {
    fun playSound(resourceName: String)
    fun stopSound()
}