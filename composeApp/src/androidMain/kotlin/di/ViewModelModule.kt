package di

import notifications.TimerNotificationManager
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import view.timer.TimerViewModel

actual val timerViewModel = module {
    viewModelOf(::TimerViewModel)
    single<TimerNotificationManager> { TimerNotificationManager() }

}