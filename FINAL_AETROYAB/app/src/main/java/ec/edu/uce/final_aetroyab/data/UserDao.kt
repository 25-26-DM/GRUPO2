package ec.edu.uce.final_aetroyab.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username AND password = :password AND isDeleted = 0 LIMIT 1")
    suspend fun validateUser(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE syncStatus = 'pending' AND isDeleted = 0")
    suspend fun getPendingSync(): List<User>

    @Query("UPDATE users SET syncStatus = :status WHERE username = :username")
    suspend fun updateSyncStatus(username: String, status: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
}
