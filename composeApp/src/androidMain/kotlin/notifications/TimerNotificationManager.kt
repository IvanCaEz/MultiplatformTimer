package notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import localization.LocalizationAndroid
import org.ivancaez.cooltimer.CoolTimerApp
import org.ivancaez.cooltimer.R

actual class TimerNotificationManager {

    private val context = CoolTimerApp.appContext

    companion object {
        const val CHANNEL_ID = "Timer_channel_id"
        const val NOTIFICATION_ID = 1
    }

    private var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    init {
        createNotificationChannel()
    }

    /**
     * Creates a notification channel for the timer notifications without sound nor vibration
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val name = "Timer Channel"
            val descriptionText = "Channel for Timer notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setSound(null, null)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Creates a notification with the remaining time and the period
     * @param remainingTime Int -> The number of seconds left in the timer, the function will format this time to mm:ss
     * @param period String -> The period of the timer, either "WarmUp", "Work" , "Rest" or "CoolDown"
     * @return Notification -> The notification to be displayed
     */
    private fun getNotification(remainingTime: String, period: String ): Notification {
        val deepLink = "CoolTimer://TimerScreen"

        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))

        val pendingIntent =
            PendingIntent.getActivity(context, 200, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(LocalizationAndroid(context).getString(period))
            .setContentText(remainingTime)
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .build()
    }

    /**
     * Updates the timer notification with the remaining time and the period
     * @param remainingTime Int -> The number of seconds left in the timer, the function will format this time to mm:ss
     * @param period String -> The period of the timer, either "WarmUp", "Work" , "Rest" or "CoolDown"
     * Then creates the notification and displays it
     */
    actual fun startTimerNotification(remainingTime: Int, period: String) {
        val timeLeft = formatTime(remainingTime)+ if (remainingTime > 59) " min" else " s"
        val notification = getNotification(timeLeft, LocalizationAndroid(context).getString(period))
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Updates the timer notification with the remaining time and the period
     * @param remainingTime Int -> The number of seconds left in the timer, the function will format this time to mm:ss
     * @param period String -> The period of the timer, either "WarmUp", "Work" , "Rest" or "CoolDown"
     * Then creates a new notification and displays it
     */
    actual fun updateTimerNotification(remainingTime: Int, period: String) {
        val timeLeft = formatTime(remainingTime)+ if (remainingTime > 59) " min" else " s"
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setContentTitle(LocalizationAndroid(context).getString(period))
            .setContentText(timeLeft)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Cancels the timer notification
     */
    actual fun stopTimerNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    /**
     * Formats the time in seconds to a string in the format "mm:ss"
     * @param seconds Int
     * @return the formatted time as a string
     */
    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return if (minutes > 0) {
            "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
        } else {
            remainingSeconds.toString()
        }
    }
}