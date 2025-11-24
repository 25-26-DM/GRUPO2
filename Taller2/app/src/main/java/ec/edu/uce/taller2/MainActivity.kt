package ec.edu.uce.taller2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ec.edu.uce.taller2.ui.theme.TALLER2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TALLER2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EventScreen()
                }
            }
        }
    }
}

@Composable
fun EventScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF24374E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1.0f))
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Logo del Evento",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Encuentro de Amigos Universitarios",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(94.dp))
        Column(modifier = Modifier.padding(horizontal = 32.dp)) {
            InfoRow(icon = Icons.Default.DateRange, text = "Sábado, 30 de noviembre de 2025",)
            InfoRow(icon = Icons.Default.LocationOn, text = "Salón de Conferencias, Universidad Central")
            InfoRow(icon = Icons.Default.Email, text = "amigos.uni2025@example.com")
            InfoRow(icon = Icons.Default.Phone, text = "+593 99 123 4567")
            InfoRow(icon = Icons.Default.Share, text = "@AmigosUni2025")
        }
        Spacer(modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun EventScreenPreview() {
    TALLER2Theme {
        EventScreen()
    }
}
