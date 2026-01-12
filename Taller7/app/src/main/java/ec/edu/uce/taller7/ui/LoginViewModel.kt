package ec.edu.uce.taller7.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var loginTime by mutableStateOf("")
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onLogin() {
        loginTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }
}