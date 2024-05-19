package ui.theme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.ivancaez.cooltimer.ui.theme.backgroundDark
import com.ivancaez.cooltimer.ui.theme.backgroundLight
import com.ivancaez.cooltimer.ui.theme.errorContainerDark
import com.ivancaez.cooltimer.ui.theme.errorContainerLight
import com.ivancaez.cooltimer.ui.theme.errorDark
import com.ivancaez.cooltimer.ui.theme.errorLight
import com.ivancaez.cooltimer.ui.theme.inverseOnSurfaceDark
import com.ivancaez.cooltimer.ui.theme.inverseOnSurfaceLight
import com.ivancaez.cooltimer.ui.theme.inversePrimaryDark
import com.ivancaez.cooltimer.ui.theme.inversePrimaryLight
import com.ivancaez.cooltimer.ui.theme.inverseSurfaceDark
import com.ivancaez.cooltimer.ui.theme.inverseSurfaceLight
import com.ivancaez.cooltimer.ui.theme.onBackgroundDark
import com.ivancaez.cooltimer.ui.theme.onBackgroundLight
import com.ivancaez.cooltimer.ui.theme.onErrorContainerDark
import com.ivancaez.cooltimer.ui.theme.onErrorContainerLight
import com.ivancaez.cooltimer.ui.theme.onErrorDark
import com.ivancaez.cooltimer.ui.theme.onErrorLight
import com.ivancaez.cooltimer.ui.theme.onPrimaryContainerDark
import com.ivancaez.cooltimer.ui.theme.onPrimaryContainerLight
import com.ivancaez.cooltimer.ui.theme.onPrimaryDark
import com.ivancaez.cooltimer.ui.theme.onPrimaryLight
import com.ivancaez.cooltimer.ui.theme.onSecondaryContainerDark
import com.ivancaez.cooltimer.ui.theme.onSecondaryContainerLight
import com.ivancaez.cooltimer.ui.theme.onSecondaryDark
import com.ivancaez.cooltimer.ui.theme.onSecondaryLight
import com.ivancaez.cooltimer.ui.theme.onSurfaceDark
import com.ivancaez.cooltimer.ui.theme.onSurfaceLight
import com.ivancaez.cooltimer.ui.theme.onSurfaceVariantDark
import com.ivancaez.cooltimer.ui.theme.onSurfaceVariantLight
import com.ivancaez.cooltimer.ui.theme.onTertiaryContainerDark
import com.ivancaez.cooltimer.ui.theme.onTertiaryContainerLight
import com.ivancaez.cooltimer.ui.theme.onTertiaryDark
import com.ivancaez.cooltimer.ui.theme.onTertiaryLight
import com.ivancaez.cooltimer.ui.theme.outlineDark
import com.ivancaez.cooltimer.ui.theme.outlineLight
import com.ivancaez.cooltimer.ui.theme.outlineVariantDark
import com.ivancaez.cooltimer.ui.theme.outlineVariantLight
import com.ivancaez.cooltimer.ui.theme.primaryContainerDark
import com.ivancaez.cooltimer.ui.theme.primaryContainerLight
import com.ivancaez.cooltimer.ui.theme.primaryDark
import com.ivancaez.cooltimer.ui.theme.primaryLight
import com.ivancaez.cooltimer.ui.theme.scrimDark
import com.ivancaez.cooltimer.ui.theme.scrimLight
import com.ivancaez.cooltimer.ui.theme.secondaryContainerDark
import com.ivancaez.cooltimer.ui.theme.secondaryContainerLight
import com.ivancaez.cooltimer.ui.theme.secondaryDark
import com.ivancaez.cooltimer.ui.theme.secondaryLight
import com.ivancaez.cooltimer.ui.theme.surfaceBrightDark
import com.ivancaez.cooltimer.ui.theme.surfaceBrightLight
import com.ivancaez.cooltimer.ui.theme.surfaceContainerDark
import com.ivancaez.cooltimer.ui.theme.surfaceContainerHighDark
import com.ivancaez.cooltimer.ui.theme.surfaceContainerHighLight
import com.ivancaez.cooltimer.ui.theme.surfaceContainerHighestDark
import com.ivancaez.cooltimer.ui.theme.surfaceContainerHighestLight
import com.ivancaez.cooltimer.ui.theme.surfaceContainerLight
import com.ivancaez.cooltimer.ui.theme.surfaceContainerLowDark
import com.ivancaez.cooltimer.ui.theme.surfaceContainerLowLight
import com.ivancaez.cooltimer.ui.theme.surfaceContainerLowestDark
import com.ivancaez.cooltimer.ui.theme.surfaceContainerLowestLight
import com.ivancaez.cooltimer.ui.theme.surfaceDark
import com.ivancaez.cooltimer.ui.theme.surfaceDimDark
import com.ivancaez.cooltimer.ui.theme.surfaceDimLight
import com.ivancaez.cooltimer.ui.theme.surfaceLight
import com.ivancaez.cooltimer.ui.theme.surfaceVariantDark
import com.ivancaez.cooltimer.ui.theme.surfaceVariantLight
import com.ivancaez.cooltimer.ui.theme.tertiaryContainerDark
import com.ivancaez.cooltimer.ui.theme.tertiaryContainerLight
import com.ivancaez.cooltimer.ui.theme.tertiaryDark
import com.ivancaez.cooltimer.ui.theme.tertiaryLight


val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val cooltimerTheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)


