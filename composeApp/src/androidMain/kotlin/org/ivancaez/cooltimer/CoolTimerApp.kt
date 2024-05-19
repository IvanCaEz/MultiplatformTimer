package org.ivancaez.cooltimer

import android.app.Application
import di.KoinInitializer

class CoolTimerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}