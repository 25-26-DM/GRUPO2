package ec.edu.uce.taller8.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ec.edu.uce.taller8.data.User
import ec.edu.uce.taller8.data.UsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult.asStateFlow()

    private val _activeUser = MutableStateFlow<User?>(null)
    val activeUser: StateFlow<User?> = _activeUser.asStateFlow()

    fun updateUiState(newUiState: LoginUiState) {
        uiState = newUiState
    }

    suspend fun login() {
        val user = usersRepository.getUser(uiState.username)
        if (user != null && user.password == uiState.password) {
            _activeUser.value = user
            _loginResult.value = LoginResult(success = true)
        } else {
            _loginResult.value = LoginResult(success = false, error = "Usuario o contraseña inválidos")
        }
    }

    suspend fun register() {
        if (uiState.username.isNotBlank() && uiState.password.isNotBlank()) {
            if (usersRepository.getUser(uiState.username) != null) {
                _loginResult.value = LoginResult(success = false, error = "El nombre de usuario ya existe")
            } else {
                val newUser = User(username = uiState.username, password = uiState.password)
                usersRepository.insertUser(newUser)
                _activeUser.value = usersRepository.getUser(uiState.username)
                _loginResult.value = LoginResult(success = true) // Auto-login after registration
            }
        } else {
             _loginResult.value = LoginResult(success = false, error = "El usuario y la contraseña no pueden estar vacíos")
        }
    }

    fun onLoginResultConsumed() {
        _loginResult.value = null
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = ""
)

data class LoginResult(
    val success: Boolean,
    val error: String? = null
)
