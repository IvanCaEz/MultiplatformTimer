package di

import org.koin.dsl.module
import view.timer.TimerViewModel

val appModule = module {
    single{ "Hello Koin!"}
    TimerViewModel("Hello Koin")
}

