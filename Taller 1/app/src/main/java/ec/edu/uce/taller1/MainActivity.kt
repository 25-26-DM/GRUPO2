package ec.edu.uce.taller1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ec.edu.uce.taller1.ui.theme.TALLER1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TALLER1Theme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF2196F3)) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {
            // Franja superior azul
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF004671))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Dispositivos Móviles",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "Grupo 1",
                        color = Color(0xFFA28F23),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Integrantes",
                    color = Color(0xFFFFD700),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp).padding(start = 32.dp),

                ) {
                Text(
                    text =  "1. Kelly Ledesma\n" +
                            "2. Edison Enriquez\n" +
                            "3. Ariel Elizalde\n" +
                            "4. Mauricio Lopez\n" +
                            "5. Alexis Troya\n" +
                            "6. Stalin Acurio\n" +
                            "7. Angelo Silva",
                    color = Color(0xFF004671),
                    fontSize = 20.sp,

                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TALLER1Theme {
        Greeting()
    }
}