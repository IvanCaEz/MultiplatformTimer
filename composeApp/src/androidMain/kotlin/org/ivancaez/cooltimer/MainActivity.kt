package org.ivancaez.cooltimer

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.ivancaez.cooltimer.database.getSessionDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionDatabase = getSessionDatabase(applicationContext)
        setContent {
            App(sessionDatabase)
        }
    }
}