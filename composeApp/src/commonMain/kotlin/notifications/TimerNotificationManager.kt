package notifications

expect class TimerNotificationManager {
    fun startTimerNotification(remainingTime: Int, period: String)
    fun updateTimerNotification(remainingTime: Int, period: String)
    fun stopTimerNotification()
}