package view.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel : ViewModel() {

    private val _langHasChanged = MutableStateFlow(false)
    val langHasChanged = _langHasChanged.asStateFlow()

    fun toggleLanguageChange() {
        _langHasChanged.value = !_langHasChanged.value
    }

    private val _visualizerHasChanged = MutableStateFlow(false)
    val visualizerHasChanged = _visualizerHasChanged.asStateFlow()

    fun toggleVisualizer() {
        _visualizerHasChanged.value = !_visualizerHasChanged.value
    }
}