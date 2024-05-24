package notifications
import platform.UserNotifications.*

actual class TimerNotificationManager {

    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private val notificationId = "timer_notification"
    actual fun startTimerNotification(remainingTime: Int, period: String) {
        val content = UNMutableNotificationContent().apply {
            title = "CoolTimer - $period"
            body = "Remaining time: $remainingTime seconds"
            sound = UNNotificationSound.soundNamed(null)
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, repeats = true)
        val request = UNNotificationRequest.requestWithIdentifier(notificationId, content, trigger)

        notificationCenter.addNotificationRequest(request) { error ->
            error?.let {
                println("Error adding notification: $it")
            }
        }
    }

    actual fun updateTimerNotification(remainingTime: Int, period: String) {
        val content = UNMutableNotificationContent().apply {
            title = "CoolTimer - $period"
            body = "Remaining time: $remainingTime seconds"
            sound = UNNotificationSound.soundNamed(null)
        }

        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(1.0, repeats = true)
        val request = UNNotificationRequest.requestWithIdentifier(notificationId, content, trigger)

        notificationCenter.addNotificationRequest(request) { error ->
            error?.let {
                println("Error updating notification: $it")
            }
        }
    }


    actual fun stopTimerNotification() {
        UNUserNotificationCenter.currentNotificationCenter().removePendingNotificationRequestsWithIdentifiers(listOf("timer_notification"))
    }
}