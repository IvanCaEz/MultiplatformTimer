package view.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {

    private val _hasChanged = MutableStateFlow(false)
    val hasChanged = _hasChanged.asStateFlow()

    fun toggleLanguageChange() {
        _hasChanged.value = !_hasChanged.value
    }
}