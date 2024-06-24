package session_visualizer

import android.content.Context
import android.content.SharedPreferences

class SessionVisualizerAndroid(context: Context): SessionVisualizer {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "MyAppPreferences", Context.MODE_PRIVATE
    )
    private val sessionVisualizerKey = "session_visualizer"

    override fun getPrefs(): Boolean {
        return prefs.getBoolean(sessionVisualizerKey, false)
    }

    override fun updatePrefs(value: Boolean) {
        prefs.edit().putBoolean(sessionVisualizerKey, value).apply()
    }
}