package localization

interface Localization {
    fun getString(key: String): String
    fun setLanguage(language: String)

    fun getLanguage(): String
}