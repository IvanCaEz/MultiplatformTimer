package org.ivancaez.cooltimer

import App
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import localization.Localization
import org.ivancaez.cooltimer.database.getSessionDatabase
import org.koin.android.ext.android.inject
import session_visualizer.SessionVisualizer

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sessionDatabase = getSessionDatabase(applicationContext)
        val localization: Localization by inject()
        val sessionVisualizer: SessionVisualizer by inject()

        setContent {
            val context = LocalContext.current
            NotificationLauncher()
            App(sessionDatabase, context, localization, sessionVisualizer)
        }
    }
}

/**
 * Checks if the user has granted the notification permission and asks for it if it hasn't been granted yet.
 * If the user has denied the permission, it will not ask for it again.
 */
@Composable
fun NotificationLauncher () {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("notification_preferences", Context.MODE_PRIVATE)

    val shouldAskPermission by remember {
        mutableStateOf(
            sharedPreferences.getBoolean("shouldAskNotificationPermission", true)
        )
    }

    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                CoolTimerApp.appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED)
        } else mutableStateOf(true)
    }
    if (shouldAskPermission && !hasNotificationPermission) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                hasNotificationPermission = isGranted
                with(sharedPreferences.edit()) {
                    putBoolean("shouldAskNotificationPermission", !isGranted)
                    apply()
                }
            }
        )

        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

}
