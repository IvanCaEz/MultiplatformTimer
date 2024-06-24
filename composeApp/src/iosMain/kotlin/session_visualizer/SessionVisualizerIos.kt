package session_visualizer
actual class SessionVisualizerIos : SessionVisualizer {

    private val userDefaults: NSUserDefaults = NSUserDefaults.standardUserDefaults
    private val sessionVisualizerKey = "session_visualizer"
    override fun getPrefs(): Boolean {
        return userDefaults.boolForKey(sessionVisualizerKey).takeIf { userDefaults.objectForKey(sessionVisualizerKey) != null } ?: false

    }

    override fun updatePrefs(value: Boolean) {
        userDefaults.setBool(sessionVisualizerKey, value)
    }
}