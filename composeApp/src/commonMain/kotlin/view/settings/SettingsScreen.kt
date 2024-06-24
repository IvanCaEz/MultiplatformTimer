package view.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ivancaez.cooltimer.ui.theme.WorkTimeColor
import cooltimer.composeapp.generated.resources.Res
import cooltimer.composeapp.generated.resources.round_english
import cooltimer.composeapp.generated.resources.round_spanish
import database.SessionDatabase
import localization.Localization
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import view.timer.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController, sessionDatabase: SessionDatabase,
    localization: Localization, timerViewModel: TimerViewModel,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var language by remember { mutableStateOf(localization.getLanguage()) }

    val languageChanged by settingsViewModel.hasChanged.collectAsState()

    if (languageChanged) {
        language = localization.getLanguage()
    }

    Scaffold (topBar = {
        TopAppBar(
            title = {
                Text(text = localization.getString("settings"))
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()

                    }.size(32.dp)
                )
            })
    }){ paddingValues ->
        Column(modifier = Modifier.fillMaxSize()
            .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageSwitcher(localization, settingsViewModel)
            SettingsItem(
                imageVector = Icons.Default.Build,
                onClick = {

                },
                text = localization.getString("show_timeline"),
                hasTint = false
            )
            SettingsItem(
                imageVector = Icons.Default.Delete,
                onClick = {
                    timerViewModel.deleteAllSessions(sessionDatabase)
                },
                text = localization.getString("delete_sessions"),
                hasTint = true
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LanguageSwitcher(localization: Localization, settingsViewModel: SettingsViewModel) {
    var language by remember { mutableStateOf(localization.getLanguage()) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(text = localization.getString("translate_button"),
            fontSize = 18.sp)

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            ){
            Image(
                painter = painterResource(Res.drawable.round_english),
                contentDescription = "UK flag",
                modifier = Modifier.size(32.dp)
            )
            Switch(checked = localization.getLanguage() == "es",
                onCheckedChange = {
                    language = if (it) {
                        "es"
                    } else {
                        "en"
                    }
                    localization.setLanguage(language)
                    settingsViewModel.toggleLanguageChange()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Image(
                painter = painterResource(Res.drawable.round_spanish),
                contentDescription = "ES flag",
                modifier = Modifier.size(32.dp)
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface
    )

}
@Composable
fun SettingsItem(imageVector: ImageVector, onClick : () -> Unit, text: String, hasTint: Boolean) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(text = text,
            fontSize = 18.sp,
            color =  if (hasTint) WorkTimeColor else MaterialTheme.colorScheme.onSurface)

        Icon(
            imageVector = imageVector,
            contentDescription = "Setting item icon",
            tint = if (hasTint) WorkTimeColor else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(32.dp)
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.onSurface
    )

}