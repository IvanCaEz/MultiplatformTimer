package view.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import soundplayer.SoundPlayer
import view.set_timer.Field

class TimerViewModel(
    private val hello: String
): ViewModel() {

    private val _remainingTime = MutableStateFlow(0)
    val remainingTime = _remainingTime.asStateFlow()

    private val _hasStarted = MutableStateFlow(false)
    val hasStarted = _hasStarted.asStateFlow()

    private val _isWorkTime = MutableStateFlow(true)
    val isWorkTime = _isWorkTime.asStateFlow()

    private var timerJob: Job? = null

    private val soundPlayer = SoundPlayer()

    private fun playSound() {
        soundPlayer.playSound("interval_sound")
    }
    private fun stopSound() {
        soundPlayer.stopSound()
    }


    fun startTimer() {
        _hasStarted.value = true
        _intervals.value = 0
        _workTimeTimer.value = _workTime.value
        _restTimeTimer.value = _restTime.value
        _isPaused.value = false
        _remainingTime.value = if (_isWorkTime.value) _workTimeTimer.value else _restTimeTimer.value
        startTimerJob()

    }

    private fun startTimerJob(){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_intervals.value != _intervalsOriginal.value) {

                // Empieza el tiempo de trabajo
                if (_isWorkTime.value) {
                    if (_isPaused.value) {
                        _remainingTime.value = _workTimeTimer.value
                    } else {
                        _remainingTime.value = _workTime.value
                    }

                    while (_remainingTime.value > -1 ) {
                        delay(1000)
                        _remainingTime.value--
                        if (_remainingTime.value == 3) {
                            playSound()
                        }
                    }

                    if (_remainingTime.value == -1) {
                        _isWorkTime.value = false
                    }
                } else {
                    if (_isPaused.value) {
                        _remainingTime.value = _restTimeTimer.value
                    } else {
                        _remainingTime.value = _restTime.value
                    }

                    while (_remainingTime.value > -1 ) {
                        delay(1000)
                        _remainingTime.value--

                        if (_remainingTime.value == 3) {
                            playSound()
                        }
                    }

                    if (_remainingTime.value == -1) {
                        _intervals.value++
                        _isWorkTime.value = true
                    }
                    if (_intervals.value == _intervalsOriginal.value) {
                        _hasStarted.value = false
                        _remainingTime.value = 0
                    }
                }
            }
        }
    }

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    fun pauseTimer() {
        _isPaused.value = true
        stopSound()
        if (_isWorkTime.value) {
            _workTimeTimer.value = _remainingTime.value
        } else {
            _restTimeTimer.value = _remainingTime.value
        }
        timerJob?.cancel()
    }

    fun resumeTimer() {
        startTimerJob()
        _isPaused.value = false
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


    private val _restTime = MutableStateFlow(0)
    val restTime = _restTime.asStateFlow()


    private val _workTime = MutableStateFlow(0)
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

    private val _selectedField = MutableStateFlow(Field.ROUNDS)
    val selectedField = _selectedField.asStateFlow()

    fun selectField(field: Field) {
        _selectedField.value = field
    }

}