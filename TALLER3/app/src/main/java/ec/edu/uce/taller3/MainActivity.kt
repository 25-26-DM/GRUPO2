package ec.edu.uce.taller3

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ec.edu.uce.taller3.ui.theme.TALLER3Theme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Forzar español como idioma por defecto si no hay uno guardado
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedLang = prefs.getString("lang", null)
        val langToSet = if (savedLang != null && savedLang in listOf("es", "en", "fr")) savedLang else "es"
        // Solo cambiar idioma si aún no está en ese idioma
        val currentLocale = resources.configuration.locales.get(0).language
        if (currentLocale != langToSet) {
            changeLanguage(this, langToSet, save = false)
        }
        setContent {
            TALLER3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

enum class AppScreen {
    MENU, DICE_ROLLER, LEMONADE
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf(AppScreen.MENU) }

    // Obtenemos el contexto y lo convertimos a Activity para poder reiniciarla
    val context = LocalContext.current
    val activity = context as? Activity

    when (currentScreen) {
        AppScreen.MENU -> {
            MenuScreen(
                onDiceRollerClick = { currentScreen = AppScreen.DICE_ROLLER },
                onLemonadeClick = { currentScreen = AppScreen.LEMONADE },
                onLanguageChange = { languageCode ->
                    activity?.let {
                        changeLanguage(it, languageCode)
                    }
                }
            )
        }
        AppScreen.DICE_ROLLER -> {
            BackHandler { currentScreen = AppScreen.MENU }
            ScreenWithBackButton(onBack = { currentScreen = AppScreen.MENU }) {
                DiceRollerScreen()
            }
        }
        AppScreen.LEMONADE -> {
            BackHandler { currentScreen = AppScreen.MENU }
            ScreenWithBackButton(onBack = { currentScreen = AppScreen.MENU }) {
                LemonadeApp()
            }
        }
    }
}

@Composable
fun ScreenWithBackButton(onBack: () -> Unit, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        Button(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.back_menu))
        }
    }
}

// --- LÓGICA PARA CAMBIAR IDIOMA SIN DEPENDENCIAS ---

fun changeLanguage(context: Context, languageCode: String, save: Boolean = true) {
    // Solo permitir es, en, fr
    val allowed = listOf("es", "en", "fr")
    val lang = if (languageCode in allowed) languageCode else "es"
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    @Suppress("DEPRECATION")
    resources.updateConfiguration(configuration, resources.displayMetrics)
    // Guardar preferencia si corresponde
    if (save) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("lang", lang).apply()
    }
    if (context is Activity) {
        context.recreate()
    }
}
