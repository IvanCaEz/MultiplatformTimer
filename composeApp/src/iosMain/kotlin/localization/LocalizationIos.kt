package localization

import Foundation

class LocalizationIos: Localization {
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val languageKey = "selected_language"

    init {
        // Configurar el idioma inicial si no está configurado
        if (userDefaults.objectForKey(languageKey) == null) {
            userDefaults.setObject(NSLocale.currentLocale.languageCode, languageKey)
            userDefaults.synchronize()
        }
    }

    override fun getString(key: String): String {
        val mainBundle = NSBundle.mainBundle
        val localizedString = mainBundle.localizedStringForKey(key, "", getLanguage())
        return localizedString ?: key
    }

    override fun setLanguage(language: String) {
        userDefaults.setObject(language, languageKey)
        userDefaults.synchronize()
    }

    override fun getLanguage(): String {
        return userDefaults.stringForKey(languageKey) ?: NSLocale.currentLocale.languageCode ?: "en"
    }
}