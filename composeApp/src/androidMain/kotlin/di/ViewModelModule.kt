package di

import localization.Localization
import localization.LocalizationAndroid
import notifications.TimerNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import view.timer.TimerViewModel

actual val platformModule = module {
    viewModelOf(::TimerViewModel)
    single<TimerNotificationManager> { TimerNotificationManager() }
    single<Localization> { LocalizationAndroid(androidContext()) }


}