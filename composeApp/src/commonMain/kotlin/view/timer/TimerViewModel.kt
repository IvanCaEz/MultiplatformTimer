package view.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerViewModel(
    private val hello: String
): ViewModel() {

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()

    private val _hasStarted = MutableStateFlow(false)
    val hasStarted = _hasStarted.asStateFlow()

    private val _isWorkTime = MutableStateFlow(true)
    val isWorkTime = _isWorkTime.asStateFlow()

    fun starTimer() {
        _hasStarted.value = true
        viewModelScope.launch {
            while (_intervals.value > 0) {
                // Empieza el tiempo de trabajo
                _workTimeTimer.value = _workTime.value
                _isWorkTime.value = true
                while (_workTimeTimer.value > 0 ) {
                    delay(1000)
                    _workTimeTimer.value--
                }
                if (_workTimeTimer.value == 0) {
                    // Empieza el tiempo de descanso
                    _restTimeTimer.value = _restTime.value
                    _isWorkTime.value = false
                    while (_restTimeTimer.value > 0) {
                        delay(1000)
                        _restTimeTimer.value--
                    }
                    if (_restTimeTimer.value == 0) {
                        _intervals.value--
                    }
                }
                if (_intervals.value == 0) {
                    _hasStarted.value = false
                }
            }
        }
    }

    fun pauseTimer() {

    }

    private val _intervals = MutableStateFlow(0)
    val intervals = _intervals.asStateFlow()

    fun addIntervals() {
        _intervals.value++
    }

    fun removeIntervals() {
        if (_intervals.value > 0) {
            _intervals.value--
        }
    }


    private val _restTime = MutableStateFlow(5)
    val restTime = _restTime.asStateFlow()


    private val _workTime = MutableStateFlow(15)
    val workTime = _workTime.asStateFlow()



    private val _restTimeTimer = MutableStateFlow(0)
    val restTimeTimer = _restTimeTimer.asStateFlow()


    private val _workTimeTimer = MutableStateFlow(0)
    val workTimeTimer = _workTimeTimer.asStateFlow()


}