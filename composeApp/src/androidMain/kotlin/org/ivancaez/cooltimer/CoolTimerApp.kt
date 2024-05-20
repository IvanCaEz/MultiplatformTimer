package org.ivancaez.cooltimer

import android.app.Application
import android.content.Context
import di.KoinInitializer

class CoolTimerApp: Application() {

    companion object {
        lateinit var appContext: Context
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        KoinInitializer(applicationContext).init()
    }
}