package ec.edu.uce.taller8.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): User?
}