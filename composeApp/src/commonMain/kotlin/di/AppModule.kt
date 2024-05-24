package di

import org.koin.dsl.module
import view.timer.TimerViewModel

val appModule = module {
    single{ TimerViewModel(get())}
}

