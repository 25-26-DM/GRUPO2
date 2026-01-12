package ec.edu.uce.taller8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import ec.edu.uce.taller8.ui.navigation.InventoryNavHost
import ec.edu.uce.taller8.ui.theme.TALLER8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

            DisposableEffect(Unit) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                onDispose { 
                    // No need to show system bars on dispose
                }
            }

            TALLER8Theme {
                InventoryNavHost()
            }
        }
    }
}