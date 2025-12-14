// kotlin
package ec.edu.uceBasicsCodelab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.uceBasicsCodelab.ui.theme.Taller5Theme

data class Member(
    val nombre1: String,
    val nombre2: String,
    val apellido: String,
    val fechaNacimiento: String,
    val direccion: String,
    val telefono: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller5Theme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

private val membersList = listOf(
    Member("Alexis", nombre2 = "Esteban", "Troya", "02/08/2004", "Calderón", "0960062255"),
    Member("Edison", "Ronaldo","Enríquez", "24/12/2000", "Carapungo", "0960986096"),
    Member("Ariel", "Marcelo", "Elizalde", "06/07/2003", "Calderón", "0985685525"),
    Member("Mauricio", nombre2 = "Alejandro", "López", "03/06/2000", "San Carlos", "0979231447"),
    Member("Kelly", "Denisse", "Ledesma", "09/08/2003", "La Bretaña", "0969892149"),
    Member("Stalin", "Vladimir", "Acurio", "13/12/1994", "Solanda", "0978657839"),
    Member("Angelo", "Martin", "Silva", "20-03-1999", "Caupicho-S51", "0987169222")
)

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    members: List<Member> = membersList
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = members) { member ->
            MemberItem(member = member)
        }
    }
}

@Composable
private fun MemberItem(member: Member, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "${member.nombre1} ${member.nombre2}")
                if (expanded) {
                    Text(text = "Nombre: ${member.nombre1}")
                    Text(text = "Apellido: ${member.apellido}")
                    Text(text = "Fecha de Nacimiento: ${member.fechaNacimiento}")
                    Text(text = "Dirección: ${member.direccion}")
                    Text(text = "Teléfono: ${member.telefono}")
                }
            }
            ElevatedButton(
                onClick = { expanded = !expanded }
            ) {
                Text(if (expanded) "Mostrar menos" else "Mostrar más")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    Taller5Theme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    Taller5Theme {
        Greetings()
    }
}

@Preview
@Composable
fun MyAppPreview() {
    Taller5Theme {
        MyApp(Modifier.fillMaxSize())
    }
}
