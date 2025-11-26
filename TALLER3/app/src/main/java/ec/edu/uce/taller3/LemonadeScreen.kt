package ec.edu.uce.taller3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Asegúrate de tener estos recursos en tu strings.xml y drawable folder
// O reemplaza los R.string y R.drawable con los que tú tengas definidos.

@Composable
fun LemonadeApp() {
    var currentStep by remember { mutableStateOf(1) }
    var squeezeCount by remember { mutableStateOf(0) }

    when (currentStep) {
        1 -> {
            LemonTextAndImage(
                textLabelResourceId = R.string.lemon_select, // "Tap the lemon tree to select a lemon"
                drawableResourceId = R.drawable.lemon_tree,
                contentDescriptionResourceId = R.string.lemon_tree_content_description,
                onImageClick = {
                    currentStep = 2
                    squeezeCount = (2..4).random()
                }
            )
        }
        2 -> {
            LemonTextAndImage(
                textLabelResourceId = R.string.lemon_squeeze, // "Keep tapping the lemon to squeeze it"
                drawableResourceId = R.drawable.lemon_squeeze,
                contentDescriptionResourceId = R.string.lemon_content_description,
                onImageClick = {
                    squeezeCount--
                    if (squeezeCount == 0) {
                        currentStep = 3
                    }
                }
            )
        }
        3 -> {
            LemonTextAndImage(
                textLabelResourceId = R.string.lemon_drink, // "Tap the lemonade to drink it"
                drawableResourceId = R.drawable.lemon_drink,
                contentDescriptionResourceId = R.string.lemonade_content_description,
                onImageClick = {
                    currentStep = 4
                }
            )
        }
        4 -> {
            LemonTextAndImage(
                textLabelResourceId = R.string.lemon_empty_glass, // "Tap the empty glass to start again"
                drawableResourceId = R.drawable.lemon_restart,
                contentDescriptionResourceId = R.string.empty_glass_content_description,
                onImageClick = {
                    currentStep = 1
                }
            )
        }
    }
}

@Composable
fun LemonTextAndImage(
    textLabelResourceId: Int,
    drawableResourceId: Int,
    contentDescriptionResourceId: Int,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(textLabelResourceId),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(drawableResourceId),
            contentDescription = stringResource(contentDescriptionResourceId),
            modifier = Modifier
                .size(200.dp) // Tamaño aproximado
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFC3ECD2)) // Un verde clarito de fondo opcional
                .clickable(onClick = onImageClick)
                .padding(24.dp)
        )
    }
}
