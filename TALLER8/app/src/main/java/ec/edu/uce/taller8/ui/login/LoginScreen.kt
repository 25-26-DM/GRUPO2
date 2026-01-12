package ec.edu.uce.taller8.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ec.edu.uce.taller8.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState
    val loginResult by viewModel.loginResult.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val unknownError = stringResource(id = R.string.invalid_credentials)

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (!it.success) {
                snackbarHostState.showSnackbar(it.error ?: unknownError)
                viewModel.onLoginResultConsumed()
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) {
        padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_personal),
                contentDescription = stringResource(R.string.group_logo_content_description),
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = uiState.username,
                        onValueChange = { viewModel.updateUiState(uiState.copy(username = it)) },
                        label = { Text(stringResource(R.string.username)) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.updateUiState(uiState.copy(password = it)) },
                        label = { Text(stringResource(R.string.password)) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = { coroutineScope.launch { viewModel.login() } }) {
                    Text(stringResource(R.string.login))
                }
                Button(onClick = { coroutineScope.launch { viewModel.register() } }) {
                    Text(stringResource(R.string.register))
                }
            }
        }
    }
}