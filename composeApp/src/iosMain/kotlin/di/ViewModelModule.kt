package di

import notifications.TimerNotificationManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val timerViewModel = module {
    singleOf(::TimerViewModel)
    single<TimerNotificationManager> { TimerNotificationManager() }
}