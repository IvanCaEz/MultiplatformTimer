package view.generic_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SquareButtonCard(
    onClick: () -> Unit,
    icon: @Composable () -> Unit) {

    Card(modifier = Modifier
            .clickable{ onClick() },
        shape = RoundedCornerShape(8.dp),) {
        icon()
    }

}