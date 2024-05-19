package view.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import view.set_timer.Field

class TimerViewModel(
    private val hello: String
): ViewModel() {

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()

    private val _hasStarted = MutableStateFlow(false)
    val hasStarted = _hasStarted.asStateFlow()

    private val _isWorkTime = MutableStateFlow(true)
    val isWorkTime = _isWorkTime.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer() {
        _hasStarted.value = true
        _intervals.value = _intervalsOriginal.value
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
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

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    fun pauseTimer() {
        _isPaused.value = true
        timerJob?.cancel()
    }

    fun resumeTimer() {
        println("RESUMIENDO")
        _isPaused.value = false
        timerJob?.start()
    }

    fun stopTimer() {
        _intervals.value = 0
        _workTimeTimer.value = 0
        _restTimeTimer.value = 0
        _hasStarted.value = false
        _isPaused.value = false
        timerJob?.cancel()

    }

    private val _intervalsOriginal = MutableStateFlow(0)
    val intervalsOriginal = _intervalsOriginal.asStateFlow()

    fun addIntervals() {
        _intervalsOriginal.value++
    }

    fun removeIntervals() {
        if (_intervalsOriginal.value > 0) {
            _intervalsOriginal.value--
        }
    }

    private val _intervals = MutableStateFlow(0)
    val intervals = _intervals.asStateFlow()


    private val _restTime = MutableStateFlow(5)
    val restTime = _restTime.asStateFlow()


    private val _workTime = MutableStateFlow(15)
    val workTime = _workTime.asStateFlow()


    // Work Time Strings
    private val _workMinutes = MutableStateFlow("00")
    val workMinutes = _workMinutes.asStateFlow()

    private val _workSeconds = MutableStateFlow("00")
    val workSeconds = _workSeconds.asStateFlow()

    // Rest Time Strings
    private val _restMinutes = MutableStateFlow("00")
    val restMinutes = _restMinutes.asStateFlow()

    private val _restSeconds = MutableStateFlow("00")
    val restSeconds = _restSeconds.asStateFlow()

    /**
     * Sets the time for the selected field
     * @param which Field, the type of Field it is
     * @param minutes String
     * @param seconds String
     */
    fun setTime(which: Field, minutes: String, seconds: String) {
        when(which) {
            Field.WORK -> {
                _workMinutes.value = minutes
                _workSeconds.value = seconds
            }
            Field.REST -> {
                _restMinutes.value = minutes
                _restSeconds.value = seconds
            }
            Field.ROUNDS -> {

            }
        }

    }

    private val _restTimeTimer = MutableStateFlow(0)
    val restTimeTimer = _restTimeTimer.asStateFlow()


    private val _workTimeTimer = MutableStateFlow(0)
    val workTimeTimer = _workTimeTimer.asStateFlow()


    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun setTimer(restTime: Int, workTime: Int) {
        _restTime.value = restTime
        _workTime.value = workTime
    }

}