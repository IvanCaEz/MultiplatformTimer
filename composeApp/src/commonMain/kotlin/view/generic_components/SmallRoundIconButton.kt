package view.generic_components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SmallRoundIconButton(onClick: () -> Unit, icon: @Composable () -> Unit, containerColor: Color? = MaterialTheme.colorScheme.primary){
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .height(42.dp)
            .width(42.dp),
        onClick = { onClick() },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = containerColor ?: MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        icon()
    }
}