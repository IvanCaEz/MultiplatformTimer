package view.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Field
import database.Session
import database.SessionDatabase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import notifications.TimerNotificationManager
import soundplayer.SoundPlayer

class TimerViewModel(
    private val timerNotificationManager: TimerNotificationManager
): ViewModel() {

    private val _sessionList = MutableStateFlow(listOf<Session>())
    val sessionList = _sessionList.asStateFlow()

    /**
     * Sets [_sessionList] to the given list
     * @param sessionList List<Session>
     */
    fun setSessionList(sessionList: List<Session>) {
        _sessionList.value = sessionList
    }

    var currentSession: Session? = null

    /**
     * Adds a session to the database
     * @param session Session
     * @param sessionDatabase SessionDatabase
     */
    fun addSession(session: Session, sessionDatabase: SessionDatabase) {
        viewModelScope.launch {
            sessionDatabase.sessionDao().upsertSession(session)
        }
    }

    /**
     * Deletes a session from the database
     * @param session Session
     * @param sessionDatabase SessionDatabase
     */
    fun deleteSession(session: Session, sessionDatabase: SessionDatabase) {
        viewModelScope.launch {
            sessionDatabase.sessionDao().deleteSession(session)
        }
    }


    private val _remainingTime = MutableStateFlow(0)
    val remainingTime = _remainingTime.asStateFlow()

    private val _hasStarted = MutableStateFlow(false)
    val hasStarted = _hasStarted.asStateFlow()

    private val _isWorkTime = MutableStateFlow(false)
    val isWorkTime = _isWorkTime.asStateFlow()

    private val _isWarmupTime = MutableStateFlow(false)
    val isWarmupTime = _isWarmupTime.asStateFlow()

    private val _isCooldownTime = MutableStateFlow(false)
    val isCooldownTime = _isCooldownTime.asStateFlow()

    private var timerJob: Job? = null

    private val soundPlayer = SoundPlayer()


    private fun playSound() {
        soundPlayer.playSound("interval_sound")
    }
    private fun stopSound() {
        soundPlayer.stopSound()
    }

    /**
     * Starts the timer setting the needed values to their initial values and calls [startTimerJob]
     */
    fun startTimer() {
        _hasStarted.value = true
        _intervals.value = 0
        _workTimeTimer.value = _workTime.value
        _restTimeTimer.value = _restTime.value
        _warmupTimeTimer.value = _warmupTime.value
        _cooldownTimeTimer.value = _cooldownTime.value
        _isPaused.value = false
        _isWarmupTime.value = _warmupTimeTimer.value != 0
        _remainingTime.value = if (_isWarmupTime.value) {
            _warmupTimeTimer.value
        } else {
            _isWorkTime.value = true
            _workTimeTimer.value
        }
        startTimerJob()


    }

    /**
     * Starts the timer job that will update the timer notification and the timer values,
     * will check what period currently is and will update the remaining time accordingly.
     */
    private fun startTimerJob(){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            timerNotificationManager.startTimerNotification(
                remainingTime = remainingTime.value,
                period = if (_isWarmupTime.value) "Calentamiento" else "Trabajo"
            )
            while (hasStarted.value) {
                // Empieza el tiempo de calentamiento
                if (_isWarmupTime.value) {
                    if (_isPaused.value) {
                        _remainingTime.value = _warmupTimeTimer.value
                    } else {
                        _remainingTime.value = _warmupTime.value
                    }
                    while (_remainingTime.value > -1 ) {
                        timerNotificationManager.updateTimerNotification(remainingTime.value, "Calentamiento")
                        delay(1000)
                        _remainingTime.value--
                        if (_remainingTime.value == 3) {
                            playSound()
                        }
                    }
                    _isWarmupTime.value = false
                    _isWorkTime.value = true

                }
                // Empieza el tiempo de enfriamiento
                else if (_isCooldownTime.value){
                    if (_isPaused.value) {
                        _remainingTime.value = _cooldownTimeTimer.value
                    } else {
                        _remainingTime.value = _cooldownTime.value
                    }
                    while (_remainingTime.value > -1 ) {
                        timerNotificationManager.updateTimerNotification(remainingTime.value, "Enfriamiento")
                        delay(1000)
                        _remainingTime.value--
                        if (_remainingTime.value == 3) {
                            playSound()
                        }
                    }
                    _isCooldownTime.value = false
                    _hasStarted.value = false
                    _remainingTime.value = 0

                }
                // Empieza el tiempo de trabajo
                else if (_isWorkTime.value) {
                    if (_isPaused.value) {
                        _remainingTime.value = _workTimeTimer.value
                    } else {
                        _remainingTime.value = _workTime.value
                    }
                    while (_remainingTime.value > -1 ) {
                        timerNotificationManager.updateTimerNotification(remainingTime.value, "Trabajo")
                        delay(1000)
                        _remainingTime.value--
                        if (_remainingTime.value == 3) {
                            playSound()
                        }
                    }
                    if (_remainingTime.value == -1) {
                        _isWorkTime.value = false
                    }
                // Empieza el tiempo de descanso
                } else {
                    if (_isPaused.value) {
                        _remainingTime.value = _restTimeTimer.value
                    } else {
                        _remainingTime.value = _restTime.value
                    }
                    while (_remainingTime.value > -1 ) {
                        timerNotificationManager.updateTimerNotification(remainingTime.value, "Descanso")
                        delay(1000)
                        _remainingTime.value--
                        if (_remainingTime.value == 3) {
                            playSound()
                        }
                    }
                    if (_remainingTime.value == -1) {
                        _intervals.value++
                        if (_intervals.value == _intervalsOriginal.value.toInt()) {
                            _remainingTime.value = 0
                            _isWorkTime.value = false
                            _isCooldownTime.value = _cooldownTime.value != 0
                            _hasStarted.value = _isCooldownTime.value
                        } else {
                            _isWorkTime.value = true
                        }
                    }
                }
            }
            timerNotificationManager.stopTimerNotification()
        }
    }

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    /**
     * Pauses the timer and saves the remaining time for the current period,
     * also stops the sound and clears the notification
     */
    fun pauseTimer() {
        _isPaused.value = true
        stopSound()
        if (_isWorkTime.value) {
            _workTimeTimer.value = _remainingTime.value
        } else if (_isCooldownTime.value){
            _cooldownTimeTimer.value = _remainingTime.value
        } else if (_isWarmupTime.value) {
            _warmupTimeTimer.value = _remainingTime.value
        }
        else {
            _restTimeTimer.value = _remainingTime.value
        }
        timerJob?.cancel()
        timerNotificationManager.stopTimerNotification()
    }

    /**
     * Resumes the timer calling [startTimerJob] and setting the value of [_isPaused] to false
     */
    fun resumeTimer() {
        startTimerJob()
        _isPaused.value = false
    }

    /**
     * Resets the timer values to their initial values in the setup screen and clears the [currentSession]
     */
    fun resetSetTimer() {
        _canProceed.value = false
        currentSession = null
        _selectedField.value = Field.NAME
        _intervalsOriginal.value = "0"
        _sessionName.value = ""
        // Work
        _workSeconds.value = "00"
        _workMinutes.value = "00"
        _workTime.value = 0

        // Rest
        _restSeconds.value = "00"
        _restMinutes.value = "00"
        _restTime.value = 0
        // Warmup
        _warmupSeconds.value = "00"
        _warmupMinutes.value = "00"
        _warmupTime.value = 0
        // Cooldown
        _cooldownSeconds.value = "00"
        _cooldownMinutes.value = "00"
        _cooldownTime.value = 0
    }

    /**
     * Stops the timer and clears the values
     */
    fun stopTimer() {
         stopSound()
        _intervals.value = 0
        _workTimeTimer.value = 0
        _restTimeTimer.value = 0
        _remainingTime.value = 0
        _hasStarted.value = false
        _isWorkTime.value = false
        _isWarmupTime.value = false
        _isCooldownTime.value = false
        _isPaused.value = false
        _canProceed.value = false
        timerJob?.cancel()
        timerNotificationManager.stopTimerNotification()
    }

    private val _sessionName = MutableStateFlow("")
    val sessionName = _sessionName.asStateFlow()

    fun setSessionName(name: String) {
        _sessionName.value = name
    }

    private val _intervalsOriginal = MutableStateFlow("0")
    val intervalsOriginal = _intervalsOriginal.asStateFlow()



    /**
     * Updates the string values with the session info and updates [currentSession] with the session to edit,
     * then calls [setTime] to update the time values
     * @param session Session
     */
    fun setSession(session: Session) {
        currentSession = session

        _intervalsOriginal.value = session.intervals.toString()

        _workSeconds.value = (session.workTime % 60).toString().padStart(2, '0')
        _workMinutes.value = (session.workTime / 60).toString().padStart(2, '0')

        _restSeconds.value = (session.restTime % 60).toString().padStart(2, '0')
        _restMinutes.value = (session.restTime / 60).toString().padStart(2, '0')

        _warmupSeconds.value = (session.warmupTime?.rem(60)).toString().padStart(2, '0')
        _warmupMinutes.value = (session.warmupTime?.div(60)).toString().padStart(2, '0')

        _cooldownSeconds.value = (session.cooldownTime?.rem(60)).toString().padStart(2, '0')
        _cooldownMinutes.value = (session.cooldownTime?.div(60)).toString().padStart(2, '0')

        _sessionName.value = session.sessionName

        setTime(Field.WARMUP, _warmupMinutes.value, _warmupSeconds.value)
        setTime(Field.WORK, _workMinutes.value, _workSeconds.value)
        setTime(Field.REST, _restMinutes.value, _restSeconds.value)
        setTime(Field.COOLDOWN, _cooldownMinutes.value, _cooldownSeconds.value)
    }

    fun addIntervals() {
        _intervalsOriginal.value = (_intervalsOriginal.value.toInt() + 1).toString()
    }

    fun removeIntervals() {
        if (_intervalsOriginal.value.toInt() > 0) {
            _intervalsOriginal.value = (_intervalsOriginal.value.toInt() - 1).toString()
        }
    }

    fun setIntervals(intervals: String) {
        _intervalsOriginal.value = intervals
    }

    private val _intervals = MutableStateFlow(0)
    val intervals = _intervals.asStateFlow()


    private val _restTime = MutableStateFlow(0)
    val restTime = _restTime.asStateFlow()


    private val _workTime = MutableStateFlow(0)
    val workTime = _workTime.asStateFlow()

    private val _warmupTime = MutableStateFlow(0)
    val warmupTime = _warmupTime.asStateFlow()


    private val _cooldownTime = MutableStateFlow(0)
    val cooldownTime = _cooldownTime.asStateFlow()

    // SetUp Screen Values

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

    // WarmUp Time Strings
    private val _warmupMinutes = MutableStateFlow("00")
    val warmupMinutes = _warmupMinutes.asStateFlow()

    private val _warmupSeconds = MutableStateFlow("00")
    val warmupSeconds = _warmupSeconds.asStateFlow()

    // Cooldown Time Strings
    private val _cooldownMinutes = MutableStateFlow("00")
    val cooldownMinutes = _cooldownMinutes.asStateFlow()

    private val _cooldownSeconds = MutableStateFlow("00")
    val cooldownSeconds = _cooldownSeconds.asStateFlow()


    /**
     * Sets the time for the selected field
     * @param which Field, the type of Field it is
     * @param minutes String
     * @param seconds String
     */
    fun setTime(which: Field, minutes: String, seconds: String) {
        when(which) {
            Field.WARMUP -> {
                _warmupMinutes.value = minutes
                _warmupSeconds.value = seconds
            }
            Field.WORK -> {
                _workMinutes.value = minutes
                _workSeconds.value = seconds
            }
            Field.REST -> {
                _restMinutes.value = minutes
                _restSeconds.value = seconds
            }
            Field.COOLDOWN -> {
                _cooldownMinutes.value = minutes
                _cooldownSeconds.value = seconds
            }
            Field.ROUNDS -> {

            }
            Field.NAME -> {

            }
        }

    }

    // Timers
    private val _warmupTimeTimer = MutableStateFlow(0)

    private val _cooldownTimeTimer = MutableStateFlow(0)

    private val _restTimeTimer = MutableStateFlow(0)

    private val _workTimeTimer = MutableStateFlow(0)

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    /**
     * Prepares the timer with the session values
     * @param session Session
     */
    fun setTimer(session: Session) {
        _intervalsOriginal.value = session.intervals.toString()
        _restTime.value = session.restTime
        _workTime.value = session.workTime
        _warmupTime.value = session.warmupTime ?: 0
        _cooldownTime.value = session.cooldownTime ?: 0
    }

    private val _selectedField = MutableStateFlow(Field.NAME)
    val selectedField = _selectedField.asStateFlow()

    fun selectField(field: Field) {
        _selectedField.value = field
    }

    // Error control

    private val _intervalsValid = MutableStateFlow(true)
    val intervalsValid = _intervalsValid.asStateFlow()

    private val _workTimeValid = MutableStateFlow(true)
    val workTimeValid = _workTimeValid.asStateFlow()

    private val _restTimeValid = MutableStateFlow(true)
    val restTimeValid = _restTimeValid.asStateFlow()

    private val _canProceed = MutableStateFlow(false)
    val canProceed = _canProceed.asStateFlow()

    /**
     * Checks if the minimun session values are valid and sets the values of the valid variables as well as [canProceed]
     * @param intervals Int -> The number of intervals
     * @param workTime Int -> The time for the work period
     * @param restTime Int -> The time for the rest period
     */
    fun validateSession(intervals: Int, workTime: Int, restTime: Int) {
        _workTimeValid.value = workTime > 0
        _restTimeValid.value = restTime > 0
        _intervalsValid.value = intervals > 0
        _canProceed.value = _workTimeValid.value && _restTimeValid.value && _intervalsValid.value
    }

    /**
     * Formats the time in seconds to a string in the format "mm:ss"
     * @param seconds Int
     * @return the formatted time as a string
     */
    fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return if (minutes > 0) {
            "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
        } else {
            remainingSeconds.toString()
        }
    }

}