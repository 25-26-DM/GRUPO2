package ec.edu.uce.taller7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ec.edu.uce.taller7.ui.MarsApp
import ec.edu.uce.taller7.ui.theme.Taller7Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Taller7Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MarsApp()
                }
            }
        }
    }
}