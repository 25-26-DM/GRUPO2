package ec.edu.uce.taller7.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ec.edu.uce.taller7.ui.screens.HomeScreen
import ec.edu.uce.taller7.ui.screens.LoginScreen
import ec.edu.uce.taller7.ui.screens.TotalScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarsApp() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val marsViewModel: MarsViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                username = loginViewModel.username,
                onUsernameChange = { loginViewModel.onUsernameChange(it) },
                onLogin = {
                    loginViewModel.onLogin()
                    navController.navigate("main")
                },
                password = loginViewModel.password,
                onPasswordChange = { loginViewModel.onPasswordChange(it) }
            )
        }
        composable("main") {
            MainScreen(loginViewModel, marsViewModel) {
                val photoCount = when (val state = marsViewModel.marsUiState) {
                    is MarsUiState.Success -> state.photos.size
                    else -> 0
                }
                navController.navigate("total/$photoCount/${loginViewModel.username}/${loginViewModel.loginTime}")
            }
        }
        composable("total/{photoCount}/{username}/{loginTime}") { backStackEntry ->
            val photoCount = backStackEntry.arguments?.getString("photoCount")?.toInt() ?: 0
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val loginTime = backStackEntry.arguments?.getString("loginTime") ?: ""
            TotalScreen(photoCount = photoCount, username = username, loginTime = loginTime)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(loginViewModel: LoginViewModel, marsViewModel: MarsViewModel, onNext: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Column {
                    Text("Mars Photos")
                    Text("Usuario: ${loginViewModel.username} - Inicio: ${loginViewModel.loginTime}", style = MaterialTheme.typography.bodySmall)
                }
            },
            actions = {
                IconButton(onClick = onNext) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente")
                }
            })
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreen(marsUiState = marsViewModel.marsUiState)
        }
    }
}