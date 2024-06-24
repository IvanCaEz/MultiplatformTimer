package di

import localization.Localization
import localization.LocalizationIos
import notifications.TimerNotificationManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import session_visualizer.SessionVisualizer
import session_visualizer.SessionVisualizerIos

actual val platformModule = module {
    singleOf(::TimerViewModel)
    single<TimerNotificationManager> { TimerNotificationManager() }
    single<Localization> { LocalizationIos() }
    single<SessionVisualizer> { SessionVisualizerIos() }

}