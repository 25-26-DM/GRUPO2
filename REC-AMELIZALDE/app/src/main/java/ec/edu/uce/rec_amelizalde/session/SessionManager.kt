package ec.edu.uce.rec_amelizalde.session

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGIN_TIME = "login_time"
        private const val KEY_LAST_ACTIVITY = "last_activity"
        private const val KEY_USERNAME = "username"
        private val MAX_SESSION_MS = TimeUnit.MINUTES.toMillis(15)
        private val INACTIVITY_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5)
    }

    fun startSession(username: String) {
        val now = System.currentTimeMillis()
        prefs.edit().putLong(KEY_LOGIN_TIME, now).putLong(KEY_LAST_ACTIVITY, now).putString(KEY_USERNAME, username).apply()
    }

    fun touch() {
        prefs.edit().putLong(KEY_LAST_ACTIVITY, System.currentTimeMillis()).apply()
    }

    fun isSessionValid(): Boolean {
        val now = System.currentTimeMillis()
        val login = prefs.getLong(KEY_LOGIN_TIME, 0L)
        val last = prefs.getLong(KEY_LAST_ACTIVITY, 0L)
        if (login == 0L) return false
        if (now - login > MAX_SESSION_MS) return false
        if (now - last > INACTIVITY_TIMEOUT_MS) return false
        return true
    }

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}