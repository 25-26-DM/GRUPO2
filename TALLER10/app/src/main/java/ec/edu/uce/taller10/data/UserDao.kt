package ec.edu.uce.taller10.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun validateUser(username: String, password: String): User?

    @Insert
    suspend fun insertUser(user: User)
}
