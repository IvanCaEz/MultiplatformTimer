package session_visualizer

interface SessionVisualizer {
    fun getPrefs(): Boolean
    fun updatePrefs(value: Boolean)
}