package di

import localization.Localization
import org.koin.dsl.module
import session_visualizer.SessionVisualizer
import view.timer.TimerViewModel

val appModule = module {
    single{ TimerViewModel(get())}
    single<Localization> { get<Localization>() }
    single<SessionVisualizer> { get<SessionVisualizer>() }
}

