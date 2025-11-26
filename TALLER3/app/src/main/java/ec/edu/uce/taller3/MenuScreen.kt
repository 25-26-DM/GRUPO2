package ec.edu.uce.taller3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onDiceRollerClick: () -> Unit,
    onLemonadeClick: () -> Unit,
    onLanguageChange: (String) -> Unit // Callback para cambiar idioma
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_app),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = onDiceRollerClick,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp)
        ) {
            Text(stringResource(R.string.dice_roller), fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onLemonadeClick,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(60.dp)
        ) {
            Text(stringResource(R.string.lemonade), fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Sección de idiomas
        Text("Idioma / Language / Langue", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { onLanguageChange("es") }) { Text("ES") }
            Button(onClick = { onLanguageChange("en") }) { Text("EN") }
            Button(onClick = { onLanguageChange("fr") }) { Text("FR") }
        }
    }
}
