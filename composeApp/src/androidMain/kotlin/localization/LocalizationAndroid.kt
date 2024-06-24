package localization

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

class LocalizationAndroid(private var context: Context) : Localization {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "MyAppPreferences", Context.MODE_PRIVATE
    )
    private val languageKey = "selected_language"

    init {
        // Configurar el idioma inicial si no está configurado
        if (!prefs.contains(languageKey)) {
            prefs.edit {
                putString(languageKey, Locale.getDefault().language)
            }
        }
    }

    override fun getString(key: String): String {
        val resources = context.resources
        val resId = resources.getIdentifier(key, "string", context.packageName)
        return if (resId != 0) {
            val config = Configuration(resources.configuration)
            config.setLocale(Locale(getLanguage()))
            val localizedContext = context.createConfigurationContext(config)
            localizedContext.resources.getString(resId)
        } else {
            key
        }
    }

    override fun setLanguage(language: String) {
        prefs.edit {
            putString(languageKey, language)
        }
    }

    override fun getLanguage(): String {
        return prefs.getString(languageKey, Locale.getDefault().language) ?: Locale.getDefault().language
    }
}